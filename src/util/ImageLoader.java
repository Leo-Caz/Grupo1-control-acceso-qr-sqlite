package util;

import javax.swing.*;
import java.io.File;

public class ImageLoader {

    private static final String PATH = "fotos_usuarios/";

    public static ImageIcon load(String filename) {
        File img = new File(PATH + filename);
        if (!img.exists()) {
            return new ImageIcon(PATH + "default.png");
        }
        return new ImageIcon(img.getAbsolutePath());
    }
}
