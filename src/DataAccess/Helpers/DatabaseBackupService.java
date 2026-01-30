package DataAccess.Helpers;

import Infrastructure.Config.BNAppConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DatabaseBackupService {

    public void crearRespaldo() {
        // 1. Obtener la ruta REAL de la base de datos actual (gracias al arreglo que hicimos)
        String rutaActual = BNAppConfig.bnGetCleanPath();
        File archivoOrigen = new File(rutaActual);

        if (!archivoOrigen.exists()) {
            JOptionPane.showMessageDialog(null, 
                "Error: No se encuentra el archivo de base de datos original en:\n" + rutaActual, 
                "Error de Respaldo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Preparar el nombre sugerido con la fecha de hoy (Ej: Respaldo_2026-01-28.sqlite)
        String fechaHoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nombreSugerido = "Respaldo_Usuario_" + fechaHoy + ".sqlite";

        // 3. Configurar el Chooser para guardar
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Copia de Seguridad");
        fileChooser.setSelectedFile(new File(nombreSugerido));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Base de Datos SQLite", "sqlite", "db");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoDestino = fileChooser.getSelectedFile();
            
            // Asegurar extensión .sqlite
            if (!archivoDestino.getAbsolutePath().endsWith(".sqlite")) {
                archivoDestino = new File(archivoDestino.getAbsolutePath() + ".sqlite");
            }

            try {
                // 4. COPIAR EL ARCHIVO (Aquí ocurre la magia)
                Files.copy(
                    archivoOrigen.toPath(), 
                    archivoDestino.toPath(), 
                    StandardCopyOption.REPLACE_EXISTING
                );

                JOptionPane.showMessageDialog(null, 
                    "¡Respaldo creado exitosamente!\nGuardado en: " + archivoDestino.getName(), 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al copiar el archivo:\n" + e.getMessage(), 
                    "Fallo en Respaldo", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
