package Camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

public class CameraServiceImpl implements CameraService {

    private Webcam webcam;

    @Override
    public void start() {
        webcam = Webcam.getDefault();
        
        if (webcam != null) {
            // Buscamos si la cámara soporta 1280x720
            Dimension[] sizes = webcam.getViewSizes();
            Dimension targetSize = new Dimension(640, 480);
            boolean supportsTarget = false;

            for (Dimension d : sizes) {
                if (d.equals(targetSize)) {
                    supportsTarget = true;
                    break;
                }
            }

            // Solo forzamos el tamaño si la cámara lo permite
            if (supportsTarget) {
                webcam.setViewSize(targetSize);
            } else {
                // Opcional: Imprimir advertencia para saber qué tamaño está usando
                System.out.println("Advertencia: La cámara no soporta 640x480. Se usará el tamaño por defecto.");
            }

            webcam.open();
        }
    }

    @Override
    public BufferedImage getFrame() {
        if (webcam != null && webcam.isOpen()) {
            return webcam.getImage();
        }
        return null;
    }

    @Override
    public void stop() {
        if (webcam != null) {
            webcam.close();
        }
    }
}
