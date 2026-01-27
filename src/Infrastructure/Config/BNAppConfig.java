package Infrastructure.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BNAppConfig {
    
    private static final Properties props = new Properties();
    private static final String APP_PROPERTIES_PATH = "src/app.properties"; 

    static {
        File file = new File(APP_PROPERTIES_PATH);
        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                props.load(input);
            } catch (IOException e) {
                System.err.println("❌ ERROR: No se pudo leer el archivo en " + APP_PROPERTIES_PATH);
            }
        } else {
            System.err.println("❌ ERROR CRÍTICO: El archivo no existe en la ruta: " + file.getAbsolutePath());
            try (InputStream stream = BNAppConfig.class.getClassLoader().getResourceAsStream("app.properties")) {
                if (stream != null) props.load(stream);
            } catch (Exception ex) { }
        }
    }

    private static final String KEY_DB_PATH     = "path.database";
    private static final String KEY_LOG_PATH    = "path.log";
    private static final String KEY_MSG_ERROR   = "msg.error";
    private static final String KEY_MSG_CLASS   = "msg.class";
    private static final String KEY_MSG_METHOD  = "msg.method";

    public static final String bnGetDatabase() { return bnGetProperty(KEY_DB_PATH); }
    public static final String bnGetLogFile()  { return bnGetProperty(KEY_LOG_PATH); }

    public static final String BN_MSG_ERROR  = bnGetProperty(KEY_MSG_ERROR);
    public static final String BN_MSG_CLASS  = bnGetProperty(KEY_MSG_CLASS);
    public static final String BN_MSG_METHOD = bnGetProperty(KEY_MSG_METHOD);

    private BNAppConfig(){}

    private static String bnGetProperty(String key) {
        String value = props.getProperty(key);
        if (value == null) return "Key '" + key + "' not found";
        return value;
    }
}
