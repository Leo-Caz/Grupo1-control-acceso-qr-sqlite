package Infrastructure.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public abstract class BNAppConfig {
    
    private static final Properties props = new Properties();
    private static final String APP_PROPERTIES_PATH = "app.properties"; 

    // ✅ VARIABLE DE MEMORIA: Guardará la ruta "limpia" (ej: C:\Escritorio\Usuario.sqlite)
    private static String rutaManual = null;

    // Bloque estático de inicialización
    static {
        File externalFile = new File(System.getProperty("user.dir") + File.separator + APP_PROPERTIES_PATH);
        boolean loaded = false;

        if (externalFile.exists()) {
            try (InputStream input = new FileInputStream(externalFile)) {
                props.load(input);
                loaded = true;
            } catch (Exception e) { 
                /* Ignorar error de lectura externo */ 
            }
        }

        if (!loaded) {
            try (InputStream stream = BNAppConfig.class.getClassLoader().getResourceAsStream("app.properties")) {
                if (stream != null) {
                    props.load(stream);
                } else {
                    System.err.println("⚠️ Advertencia: No se encontro app.properties interno.");
                }
            } catch (Exception ex) { 
                ex.printStackTrace();
            }
        }
    } 

    private static final String KEY_DB_PATH     = "path.database";
    private static final String KEY_LOG_PATH    = "path.log";
    private static final String KEY_MSG_ERROR   = "msg.error";
    private static final String KEY_MSG_CLASS   = "msg.class";
    private static final String KEY_MSG_METHOD  = "msg.method";

    // ✅ MÉTODO 1: SETTER
    // Recibe la ruta limpia desde DataHelperSQLite
    public static void setCustomDBPath(String nuevaRutaLimpia) {
        rutaManual = nuevaRutaLimpia;
    }

    // ✅ MÉTODO 2: GETTER PARA SQL (Conexión)
    // Este lo usa DataHelper para conectarse. Le agrega el prefijo.
    public static final String bnGetDatabase() {
        return "jdbc:sqlite:" + bnGetCleanPath();
    }

    // ✅ MÉTODO 3: GETTER PARA ARCHIVOS (Exportar)
    // Este calcula la ruta real del archivo sin el "jdbc:sqlite:"
    public static final String bnGetCleanPath() {
        // A. Si se seleccionó manualmente (Escritorio), devolvemos esa.
        if (rutaManual != null) {
            return rutaManual;
        }

        // B. Si no, calculamos la ruta original del properties
        String dbPathConfig = props.getProperty(KEY_DB_PATH, "Database/Usuario.sqlite");
        
        // Verificación de ruta absoluta
        if (dbPathConfig.contains(":") || dbPathConfig.startsWith("/")) {
            return dbPathConfig;
        }

        // Ruta relativa (Portable)
        String rutaBase = System.getProperty("user.dir");
        String rutaArchivo = dbPathConfig.replace("/", File.separator).replace("\\", File.separator);
        
        return rutaBase + File.separator + rutaArchivo;
    }

    public static final String bnGetLogFile() {
        String logRelativo = props.getProperty(KEY_LOG_PATH, "Logs/asistencia_error.log");
        String rutaBase = System.getProperty("user.dir");
        String rutaArchivo = logRelativo.replace("/", File.separator).replace("\\", File.separator);
        
        return rutaBase + File.separator + rutaArchivo;
    }

    public static final String BN_MSG_ERROR  = bnGetProperty(KEY_MSG_ERROR);
    public static final String BN_MSG_CLASS  = bnGetProperty(KEY_MSG_CLASS);
    public static final String BN_MSG_METHOD = bnGetProperty(KEY_MSG_METHOD);

    private BNAppConfig(){}

    private static String bnGetProperty(String key) {
        return props.getProperty(key, "Mensaje no definido");
    }
}