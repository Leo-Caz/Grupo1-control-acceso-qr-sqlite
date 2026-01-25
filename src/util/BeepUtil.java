package util;

import java.awt.Toolkit;

public class BeepUtil {

    public static void success() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static void error() {
        Toolkit.getDefaultToolkit().beep();
    }
}

