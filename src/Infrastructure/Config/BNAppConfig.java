package Infrastructure.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public abstract class BNAppConfig {
    
    private static final Properties props = new Properties();
    private static final String BN_APP_PROPERTIES = "src/app.properties"; 

    static {
        try (InputStream appProperties = new FileInputStream(BN_APP_PROPERTIES)) {
            props.load(appProperties);
        } catch (IOException e) {
            System.err.println("❌ ERROR CRÍTICO: No se pudo cargar " + BN_APP_PROPERTIES);
             try (InputStream stream = BNAppConfig.class.getClassLoader().getResourceAsStream("bn_app.properties")) {
                if (stream != null) props.load(stream);
            } catch (Exception ex) { }
        }
    }

    private static final String KEY_DB_PATH     = "path.database"; // Tu SQLite
    private static final String KEY_LOG_PATH    = "path.log";      // Tus logs de errores
    
    private static final String KEY_MSG_ERROR   = "msg.error";
    private static final String KEY_MSG_CLASS   = "msg.class";
    private static final String KEY_MSG_METHOD  = "msg.method";

    public static final String bnGetDatabase() { return bnGetProperty(KEY_DB_PATH); }
    public static final String bnGetLogFile() { return bnGetProperty(KEY_LOG_PATH); }

    public static final String BN_MSG_ERROR  = bnGetProperty(KEY_MSG_ERROR);
    public static final String BN_MSG_CLASS  = bnGetProperty(KEY_MSG_CLASS);
    public static final String BN_MSG_METHOD = bnGetProperty(KEY_MSG_METHOD);

    private BNAppConfig(){}

    // Método auxiliar seguro
    private static String bnGetProperty(String key) {
        String value = props.getProperty(key);
        if (value == null) return "Key '" + key + "' not found";
        return value;
    }
    
    public static URL bnGetAppResource(String key) {
        String path = bnGetProperty(key);
        if (path != null) return BNAppConfig.class.getResource(path);
        return null;
    }
}
