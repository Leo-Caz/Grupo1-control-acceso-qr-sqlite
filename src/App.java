import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Controller.AccessController;
import Infrastructure.Config.BNAppException;
import Infrastructure.Config.BNAppMSG;
import UserInterface.SwingMainWindow; 

public class App {
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el estilo nativo.");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Iniciando Sistema de Control de Acceso...");
                
                SwingMainWindow window = new SwingMainWindow();
                
                AccessController controller = new AccessController(window);
                
                window.setController(controller);
                
                window.setVisible(true);
                controller.start();
                
            } catch (Exception e) {
                new BNAppException(e, "App", "main.invokeLater");
                
                BNAppMSG.bnShowError("Error fatal al iniciar la aplicación. Revise los logs.");
                e.printStackTrace(); 
            }
        });
    }
}