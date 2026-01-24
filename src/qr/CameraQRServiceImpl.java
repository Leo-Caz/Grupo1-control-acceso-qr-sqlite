package qr;

import camera.CameraService;
import camera.CameraServiceImpl;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class CameraQRServiceImpl implements QRService {

    private CameraService camera = new CameraServiceImpl();

    @Override
    public void start() {
        camera.start();
    }

    @Override
    public BufferedImage getFrame() {
        return camera.getFrame();
    }

    @Override
    public Optional<String> tryDecode(BufferedImage frame) {
        return ZXingDecoder.decode(frame);
    }

    @Override
    public void stop() {
        camera.stop();
    }
}
