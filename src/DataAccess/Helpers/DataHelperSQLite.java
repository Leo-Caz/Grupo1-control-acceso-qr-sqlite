/*--------------------------------------------------\
|  Copyright (©) 2K25 EPN-FIS. All rights reserved  |
|  pat_mic@hotmail.com                  pat_mic     |
\--------------------------------------------------*/
package DataAccess.Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public abstract class DataHelperSQLite {
    private static final String DBPathConnection = "jdbc:sqlite:Database/Usuario.sqlite"; 
    private static Connection conn = null;
    
    private DateTimeFormatter   dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
    private LocalDateTime       now = LocalDateTime.now();

    protected DataHelperSQLite(){}
    protected static synchronized Connection openConnection() throws Exception{
        try {
            // Aseguramos que la conexión no esté cerrada antes de entregarla
            if(conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DBPathConnection);
                // IMPORTANTE: Habilitar llaves foráneas en cada sesión
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al conectar a la base de datos: " + e.getMessage());
        } 
        return conn;
    }

    protected static void closeConnection() throws Exception{
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            throw new Exception("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

    protected String getDataTimeNow() {
        return dtf.format(now).toString();
    }
    
}
