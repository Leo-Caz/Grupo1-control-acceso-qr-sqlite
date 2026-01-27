package Infrastructure.Config;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Infrastructure.Tools.CMDColor; 

public class BNAppException extends Exception {

    private static final DateTimeFormatter BN_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor 1: Mensaje simple
     */
    public BNAppException(String bnShowMsg) {
        super((bnShowMsg == null || bnShowMsg.isBlank()) ? BNAppConfig.BN_MSG_ERROR : bnShowMsg);
        bnSaveLogFile(null, null, null);
    }

    /**
     * Constructor 2: Captura de Error Técnico (Usado por el DAO)
     */
    public BNAppException(Exception e, String bnClaseNombre, String bnMetodo) {
        // Validamos que la excepción no sea nula para evitar NullPointerException
        super((e != null && e.getMessage() != null) ? e.getMessage() : BNAppConfig.BN_MSG_ERROR);
        
        bnSaveLogFile((e != null ? e.getMessage() : "Excepción Nula Detectada"), bnClaseNombre, bnMetodo);
    }

    /**
     * Método Privado: Escribir en Log y Consola
     */
    private void bnSaveLogFile(String bnLogMsg, String bnClase, String bnMetodo) {
        String bnTimestamp = LocalDateTime.now().format(BN_FORMATTER);
        
        String finalClass  = (bnClase == null)  ? BNAppConfig.BN_MSG_CLASS  : bnClase;
        String finalMethod = (bnMetodo == null) ? BNAppConfig.BN_MSG_METHOD : bnMetodo;
        String finalMsg    = (bnLogMsg == null || bnLogMsg.isBlank()) ? BNAppConfig.BN_MSG_ERROR : bnLogMsg;

        String logFormatted = String.format("╭─😵‍💫─ SHOW ❱❱ %s \n╰──── LOG  ❱❱ %s | %s.%s | %s", 
                getMessage(), 
                bnTimestamp, 
                finalClass, 
                finalMethod, 
                finalMsg);

        try (PrintWriter writer = new PrintWriter(new FileWriter(BNAppConfig.bnGetLogFile(), true))) {
            
            System.err.println(CMDColor.BLUE + logFormatted); 
            
            writer.println(logFormatted);
            writer.println("--------------------------------------------------"); 
            
        } catch (Exception e) {
            System.err.println("[BNAppException.saveLogFile] ERROR CRÍTICO ❱ " + e.getMessage());
        } finally {
            System.out.println(CMDColor.RESET); 
        }
    }
}
