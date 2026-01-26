package UI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainTestUI {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        System.out.println("Nimbus no disponible");
        }

        SwingUtilities.invokeLater(() -> {
        SwingMainWindow ventana = new SwingMainWindow();
        ventana.setVisible(true);
        });
    }
}
