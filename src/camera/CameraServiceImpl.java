package camera;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

public class CameraServiceImpl implements CameraService {

    private Webcam webcam;

    @Override
    public void start() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
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
