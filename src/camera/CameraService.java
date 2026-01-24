package camera;

import java.awt.image.BufferedImage;

public interface CameraService {
    void start();
    BufferedImage getFrame();
    void stop();
}
