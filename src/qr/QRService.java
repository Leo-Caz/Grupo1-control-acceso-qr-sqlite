package qr;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface QRService {
    void start();
    BufferedImage getFrame();
    Optional<String> tryDecode(BufferedImage frame);
    void stop();
}
