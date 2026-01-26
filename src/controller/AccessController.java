package controller;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import DataAccess.CarnetDAO;
import DataAccess.DTO.CarnetDTO;
import DataAccess.DTO.UsuarioDTO;
import DataAccess.RegistroDAO;
import DataAccess.Reports.AsistenciaCsvExporter;
import DataAccess.UsuarioDAO;
import camera.CameraService;
import camera.CameraServiceImpl;
import qr.ZXingDecoder;
import ui.UIContract;

public class AccessController {

    private final UIContract view;
    private final CameraService cameraService;
    private final ScheduledExecutorService executor;
    
    // DAOs
    private final UsuarioDAO usuarioDAO;
    private final CarnetDAO carnetDAO;
    private final RegistroDAO registroDAO;

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
        this.usuarioDAO = new UsuarioDAO();
        this.carnetDAO = new CarnetDAO();
        this.registroDAO = new RegistroDAO();
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

        try {
            // Buscar Carnet (Método corregido según tu CarnetDAO)
            CarnetDTO carnet = carnetDAO.readByCodigoQR(qrCode);

            if (carnet != null && "A".equals(carnet.getEstado())) {
                // Buscar Usuario
                UsuarioDTO usuario = usuarioDAO.readById(carnet.getIdUsuario());
                
                if (usuario != null && "A".equals(usuario.getEstado())) {
                    // --- ACCESO CONCEDIDO ---
                    
                    // Registrar en BD (Usando método optimizado o estándar)
                    registroDAO.createByQr(qrCode); 
                    
                    // Actualizar UI
                    SwingUtilities.invokeLater(() -> {
                        view.showGranted(usuario, qrCode);
                        Toolkit.getDefaultToolkit().beep(); // Sonido
                    });
                } else {
                    SwingUtilities.invokeLater(() -> view.showDenied("USUARIO INACTIVO", qrCode));
                }
            } else {
                SwingUtilities.invokeLater(() -> view.showDenied("QR NO REGISTRADO", qrCode));
            }

        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> view.showDenied("ERROR BD", qrCode));
        }
    }
    
    public void exportCsvDefault() {
        try {
            view.mostrarEstado("GENERANDO CSV...");
            AsistenciaCsvExporter exporter = new AsistenciaCsvExporter();
            // Exporta a la raíz del proyecto
            exporter.export(Paths.get("asistencia.csv"));
            
            view.mostrarEstado("CSV EXPORTADO: asistencia.csv");
            // Restaurar estado después de 2 segundos
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