package Controller;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import BusinessLogic.Entities.AsistenciaBL;
import Camera.CameraService;
import Camera.CameraServiceImpl;
import DataAccess.DTO.UsuarioDTO;
import QR.ZXingDecoder;
import UI.UIContract;

public class AccessController {

    private final UIContract view;
    private final CameraService cameraService;
    private final ScheduledExecutorService executor;
    
    private final AsistenciaBL asistenciaBL;

    // Estado del sistema
    private boolean isRunning = false;
    private String lastQrCode = "";
    private long lastAccessTime = 0;
    private static final long COOLDOWN_MS = 3000; // 3 segundos de espera entre lecturas

    public AccessController(UIContract view) {
        this.view = view;
        this.cameraService = new CameraServiceImpl();
        // Usamos un solo hilo para procesar el video secuencialmente
        this.executor = Executors.newSingleThreadScheduledExecutor();
        
        // Inicializar DAOs
        this.asistenciaBL = new AsistenciaBL();
    }

    public void start() {
        if (isRunning) return;
        
        try {
            cameraService.start();
            isRunning = true;
            view.mostrarEstado("CÁMARA INICIADA - ESPERANDO QR");
            
            // Ejecutar el ciclo de lectura cada 33ms (~30 FPS)
            executor.scheduleAtFixedRate(this::processFrame, 0, 33, TimeUnit.MILLISECONDS);
            
        } catch (Exception e) {
            view.mostrarEstado("ERROR CAMARA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processFrame() {
        if (!isRunning) return;

        try {
            // 1. Obtener frame
            BufferedImage frame = cameraService.getFrame();
            if (frame == null) return;

            // 2. Actualizar UI (enviamos al hilo de Swing)
            SwingUtilities.invokeLater(() -> view.updateFrame(frame));

            // 3. Decodificar QR
            // Tu ZXingDecoder devuelve Optional<String>
            String qrCode = ZXingDecoder.decode(frame).orElse(null);

            if (qrCode != null && !qrCode.isEmpty()) {
                handleQrDetection(qrCode);
            }

        } catch (Exception e) {
            System.err.println("Error en loop de video: " + e.getMessage());
        }
    }

    private void handleQrDetection(String qrCode) {
        long now = System.currentTimeMillis();

        // 4. Validar Cooldown (Evitar múltiples registros del mismo QR en < 4 seg)
        if (qrCode.equals(lastQrCode) && (now - lastAccessTime < COOLDOWN_MS)) {
            return;
        }

        lastQrCode = qrCode;
        lastAccessTime = now;

       // Delegamos toda la validación a la capa de negocio (BL)
        try {
            // La BL se encarga de validar carnet, usuario y registrar.
            // Si algo falla, lanza una excepción con el mensaje exacto.
            UsuarioDTO usuario = asistenciaBL.registrarAcceso(qrCode);

            // Si no lanzó excepción, es ÉXITO:
            SwingUtilities.invokeLater(() -> {
                view.showGranted(usuario, qrCode);
                Toolkit.getDefaultToolkit().beep();
            });

        } catch (Exception e) {
            // Si la BL lanzó error (Usuario inactivo, QR no existe, etc.)
            // Capturamos el mensaje (e.getMessage()) y lo mostramos.
            SwingUtilities.invokeLater(() -> view.showDenied(e.getMessage(), qrCode));
            
            // Opcional: imprimir el stacktrace solo si es un error de sistema y no de validación
            if (!e.getMessage().equals("QR NO REGISTRADO") && !e.getMessage().equals("USUARIO INACTIVO")) {
                 e.printStackTrace();
            }
        }
    }
    
    public void exportCsvDefault() {
        try {
            view.mostrarEstado("GENERANDO CSV...");
            
            // Delegamos la exportación a la BL
            asistenciaBL.generarReporteCsv(Paths.get("asistencia.csv"));
            
            view.mostrarEstado("CSV EXPORTADO: asistencia.csv");
            
            new Thread(() -> {
                try { Thread.sleep(2000); } catch (Exception e){}
                SwingUtilities.invokeLater(view::limpiarPantalla);
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
            view.mostrarEstado("ERROR EXPORTAR: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
        if (cameraService != null) cameraService.stop();
        if (executor != null) executor.shutdownNow();
        System.out.println("Sistema detenido correctamente.");
    }
}