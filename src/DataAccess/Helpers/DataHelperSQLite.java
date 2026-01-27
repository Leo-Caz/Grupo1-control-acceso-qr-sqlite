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

import Infrastructure.Config.BNAppConfig; 
import Infrastructure.Config.BNAppException; 

public abstract class DataHelperSQLite {
    // ✅ Ahora la ruta se obtiene de BNAppConfig
    private static final String DBPathConnection = BNAppConfig.bnGetDatabase(); 
    private static Connection conn = null;
    
    private DateTimeFormatter   dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
    private LocalDateTime       now = LocalDateTime.now();

    protected DataHelperSQLite(){}

    /**
     * Abre la conexión usando la ruta centralizada y activa FKs.
     */
    protected static synchronized Connection openConnection() throws BNAppException {
        try {
            if(conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DBPathConnection);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
            }
        } catch (SQLException e) {
            // ✅ Uso del nuevo sistema de errores con trazabilidad técnica
            throw new BNAppException(e, "DataHelperSQLite", "openConnection()");
        } 
        return conn;
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
