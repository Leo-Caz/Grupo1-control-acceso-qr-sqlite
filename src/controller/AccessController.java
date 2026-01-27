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
import Infrastructure.Config.BNAppMSG;
import QR.ZXingDecoder;
import UserInterface.UIContract;

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
        if (isRunning) {
            return;
        }

        try {
            cameraService.start();
            isRunning = true;
            view.mostrarEstado("CÁMARA INICIADA - ESPERANDO QR");

            // Ejecutar el ciclo de lectura cada 33ms (~30 FPS)
            executor.scheduleAtFixedRate(this::processFrame, 0, 150, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            view.mostrarEstado("ERROR CAMARA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processFrame() {
        if (!isRunning) {
            return;
        }

        try {
            // 1. Obtener frame
            BufferedImage frame = cameraService.getFrame();
            if (frame == null) {
                return;
            }

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

        // 1. Limpiar el código (Trim) para evitar errores por espacios invisibles
        String cleanQr = (qrCode != null) ? qrCode.trim() : "";
        if (cleanQr.isEmpty()) {
            return;
        }

        // 2. Validar Cooldown (Evitar múltiples registros en < 3 seg)
        // Usamos el código limpio para comparar
        if (cleanQr.equals(lastQrCode) && (now - lastAccessTime < COOLDOWN_MS)) {
            return;
        }

        // Actualizamos el estado para la siguiente lectura
        lastQrCode = cleanQr;
        lastAccessTime = now;

        // 3. Intento de registro único en Capa de Negocio
        try {
            // Una SOLA llamada y dentro del try-catch
            UsuarioDTO usuario = asistenciaBL.registrarAcceso(cleanQr);

            // Si llegó aquí, es ÉXITO:
            SwingUtilities.invokeLater(() -> {
                view.showGranted(usuario, cleanQr);
                Toolkit.getDefaultToolkit().beep();
            });

        } catch (Exception e) {
            // Captura errores: "QR NO REGISTRADO", "USUARIO INACTIVO", etc.
            SwingUtilities.invokeLater(() -> view.showDenied(e.getMessage(), cleanQr));

            // Limpiamos el lastQrCode en caso de error para que permita re-intentar rápido
            lastQrCode = "";

            if (!e.getMessage().equals("QR NO REGISTRADO") && !e.getMessage().equals("USUARIO INACTIVO")) {
                e.printStackTrace();
            }
        }
    }

    public void exportCsvDefault() {
        try {
            view.mostrarEstado("GENERANDO CSV...");
            asistenciaBL.generarReporteCsv(Paths.get("asistencia.csv"));

            BNAppMSG.bnShow("El reporte de asistencia ha sido generado exitosamente.");

            view.mostrarEstado("CSV EXPORTADO: asistencia.csv");
        } catch (Exception e) {
            BNAppMSG.bnShowError("Fallo en la exportación: " + e.getMessage());
            view.mostrarEstado("ERROR EXPORTAR");
        }
    }

    public void stop() {
        isRunning = false;
        if (cameraService != null) {
            cameraService.stop();
        }
        if (executor != null) {
            executor.shutdownNow();
        }
        System.out.println("Sistema detenido correctamente.");
    }
}
