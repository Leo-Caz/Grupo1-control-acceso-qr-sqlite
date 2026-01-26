import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.AccessController;
import ui.SwingMainWindow;

public class App {
    public static void main(String[] args) {
        // Estética nativa del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cargar el estilo nativo.");
        }

        // Iniciar la aplicación gráfica
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🚀 Iniciando Sistema de Control de Acceso...");
                
                // 1. Crear la Vista (Ventana)
                SwingMainWindow window = new SwingMainWindow();
                
                // 2. Crear el Controlador (Cerebro) e inyectar la Vista
                AccessController controller = new AccessController(window);
                
                // 3. Conectar la Vista con el Controlador
                window.setController(controller);
                
                // 4. Mostrar ventana y arrancar la cámara
                window.setVisible(true);
                controller.start();
                
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("❌ Error fatal al iniciar la aplicación.");
            }
        });
    }
}