package QR;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class MockQRService implements QRService {

    @Override
    public void start() { }

    @Override
    public BufferedImage getFrame() {
        return null; // mock de frame
    }

    @Override
    public Optional<String> tryDecode(BufferedImage frame) {
        return Optional.of("12345678"); // QR ficticio
    }

    @Override
    public void stop() { }
}
