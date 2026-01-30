/*--------------------------------------------------\
|  Copyright (©) 2K25 EPN-FIS. All rights reserved  |
|  pat_mic@hotmail.com                  pat_mic     |
\--------------------------------------------------*/
package DataAccess.Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Infrastructure.Config.BNAppConfig; 
import Infrastructure.Config.BNAppException; 

public abstract class DataHelperSQLite {
    
    // Obtenemos la ruta inicial (puede ser portable o fija desde properties)
    private static String DBPathConnection = BNAppConfig.bnGetDatabase(); 
    
    private static Connection conn = null;
    private DateTimeFormatter   dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
    private LocalDateTime       now = LocalDateTime.now();

    protected DataHelperSQLite(){}

    /**
     * Abre la conexión. Si falla la ruta por defecto, abre un selector de archivos.
     */
    protected static synchronized Connection openConnection() throws BNAppException {
        try {
            if(conn == null || conn.isClosed()) {
                
                // 1. INTENTO AUTOMÁTICO
                try {
                    conn = DriverManager.getConnection(DBPathConnection);
                } catch (SQLException e) {
                    // 2. SI FALLA (Ruta incorrecta o movida), ACTIVAMOS MODO MANUAL
                    System.err.println("⚠️ Error conectando a: " + DBPathConnection);
                    conn = conectarManualmente(); // <--- Aquí ocurre la magia
                }

                // 3. Verificamos si logramos conectar
                if (conn != null) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("PRAGMA foreign_keys = ON;");
                    }
                } else {
                    throw new SQLException("Operacion cancelada: No se selecciono base de datos.");
                }
            }
        } catch (SQLException e) {
            throw new BNAppException(e, "DataHelperSQLite", "openConnection()");
        } 
        return conn;
    }

    private static Connection conectarManualmente() {
        // 1. Avisar al usuario
        JOptionPane.showMessageDialog(null, 
            "No se encontro la base de datos en la ruta esperada.\n" +
            "Por favor, selecciona el archivo 'Usuario.sqlite' manualmente.", 
            "Base de Datos no encontrada", 
            JOptionPane.WARNING_MESSAGE);

        // 2. Configurar la ventana de búsqueda
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Busca el archivo Usuario.sqlite");
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Base de Datos SQLite", "sqlite", "db");
        fileChooser.setFileFilter(filter);

        // 3. Mostrar la ventana
        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            java.io.File dbFile = fileChooser.getSelectedFile();
            
            // --- CORRECCIÓN CRÍTICA PARA EL EXPORTAR ---
            
            // A. Obtenemos la ruta "limpia" (C:\Users\...\Usuario.sqlite)
            String rutaLimpia = dbFile.getAbsolutePath();

            // B. Guardamos la ruta limpia en la configuración (Para que Exportar la encuentre)
            BNAppConfig.setCustomDBPath(rutaLimpia); 
            
            // C. Preparamos la conexión SQL (Agregando jdbc:sqlite:)
            DBPathConnection = "jdbc:sqlite:" + rutaLimpia;
            
            System.out.println("✅ Nueva ruta establecida: " + DBPathConnection);
            
            try {
                return DriverManager.getConnection(DBPathConnection);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "El archivo seleccionado no es valido o esta corrupto.");
                return null;
            }
        } else {
            return null; // Usuario canceló
        }
    }

    protected static void closeConnection() throws BNAppException {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            throw new BNAppException(e, "DataHelperSQLite", "closeConnection()");
        }
    }

    protected String getDataTimeNow() {
        return dtf.format(now);
    }
}