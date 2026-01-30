package Camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import com.github.sarxos.webcam.Webcam;

public class CameraServiceImpl implements CameraService {

    private Webcam webcam;

   @Override
    public void start() {
        try {
            // 1. Obtener TODAS las cámaras detectadas
            List<Webcam> cams = Webcam.getWebcams();
            
            // 2. Si la lista está vacía, no hay nada que hacer.
            if (cams.isEmpty()) {
                System.out.println("⚠️ Sistema: No se detectaron camaras fisicas.");
                return;
            }

            // 3. BUSCAR UNA CÁMARA REAL (Filtrar virtuales)
            // Muchas veces el error lo causa "OBS Virtual Camera" o drivers genéricos
            for (Webcam cam : cams) {
                String nombre = cam.getName().toLowerCase();
                System.out.println("🔎 Analizando camara: " + cam.getName());

                // Lista negra de palabras clave de cámaras virtuales/problemáticas
                if (nombre.contains("obs") || nombre.contains("virtual") || nombre.contains("droidcam")) {
                    System.out.println("🚫 Ignorando camara virtual: " + cam.getName());
                    continue; 
                }

                // Si pasamos el filtro, elegimos esta cámara
                this.webcam = cam;
                break;
            }

            // 4. Si después del filtro no nos queda ninguna cámara...
            if (this.webcam == null) {
                System.out.println("⚠️ No se encontro ninguna cámara física válida.");
                return;
            }

            // 5. CONFIGURACIÓN SEGURA
            // Forzamos el primer tamaño disponible para evitar el error de buffer
            Dimension[] sizes = webcam.getViewSizes();
            if (sizes.length > 0) {
                webcam.setViewSize(sizes[0]);
            }

            // 6. INTENTO DE APERTURA SILENCIOSO
            // Redirigimos el error estándar momentáneamente para que no inunde la consola si falla el driver nativo
            java.io.PrintStream originalErr = System.err;
            try {
                // Truco sucio: Anular System.err durante el open() para tragar el spam de C++
                System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                    public void write(int b) {}
                }));
                
                webcam.open();
                
            } catch (Exception e) {
                // Falló silenciosamente
            } finally {
                // Restauramos la consola para ver errores normales
                System.setErr(originalErr);
            }

            if (webcam.isOpen()) {
                System.out.println("✅ Camara iniciada: " + webcam.getName());
            } else {
                System.out.println("⚠️ La camara existe pero no pudo iniciar video (Driver nativo fallo).");
                // Importante: Marcar webcam como null para que el resto de la app sepa que no hay video
                webcam = null; 
            }

        } catch (Throwable t) {
            System.out.println("❌ Error fatal en modulo de camara: " + t.getMessage());
        }
    }

    @Override
    public BufferedImage getFrame() {
        if (webcam != null && webcam.isOpen()) {
            try {
                return webcam.getImage();
            } catch (Exception e) {
                System.err.println("Error leyendo frame: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    public void stop() {
        if (webcam != null) {
            try {
                webcam.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar camara: " + e.getMessage());
            }
        }
    }
}
