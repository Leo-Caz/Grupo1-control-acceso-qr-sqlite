import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Scanner;

import camera.CameraService;
import camera.CameraServiceImpl;
import qr.ZXingDecoder;

public class AppBootstrap {
    public static void main(String[] args) {
        System.out.println("🎥 Prueba Cámara + ZXing");
        CameraService camera = new CameraServiceImpl();
        camera.start();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("✅ ENTER para QR, 'q' para salir:");
        
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) break;
            
            BufferedImage frame = camera.getFrame();
            if (frame == null) {
                System.out.println("❌ Sin frame");
                continue;
            }
            //si quieren cambiar los emojis con windows + . les sale para cambiar
            Optional<String> qr = ZXingDecoder.decode(frame);
            if (qr.isPresent()) {
                System.out.println("✅ QR: " + qr.get());
            } else {
                System.out.println("❌ Sin QR");
            }
        }
        
        camera.stop();
        scanner.close();
    }
}
