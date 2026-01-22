package ui;

import java.awt.image.BufferedImage;
import model.Usuario;

public interface UIContract {

    void updateVideoFrame(BufferedImage frame);

    void showAccessGranted(Usuario usuario, BufferedImage foto);

    void showAccessDenied(String message);

    void showStatus(String message);
}
