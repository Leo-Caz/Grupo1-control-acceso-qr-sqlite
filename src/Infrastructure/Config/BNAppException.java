package Infrastructure.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Infrastructure.Tools.CMDColor; 

public class BNAppException extends Exception {

    private static final DateTimeFormatter BN_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BNAppException(String bnShowMsg) {
        super((bnShowMsg == null || bnShowMsg.isBlank()) ? BNAppConfig.BN_MSG_ERROR : bnShowMsg);
        bnSaveLogFile(null, null, null);
    }

    public BNAppException(Exception e, String bnClaseNombre, String bnMetodo) {
        super(BNAppConfig.BN_MSG_ERROR); 
        bnSaveLogFile((e != null ? e.getMessage() : "Excepción Nula Detectada"), bnClaseNombre, bnMetodo);
    }

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

        String logPath = BNAppConfig.bnGetLogFile();
        File logFile = new File(logPath);
        if (logFile.getParentFile() != null && !logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(logPath, true))) {
            System.err.println(CMDColor.BLUE + logFormatted + CMDColor.RESET); 
            writer.println(logFormatted);
            writer.println("--------------------------------------------------"); 
        } catch (Exception e) {
            System.err.println(CMDColor.RED + "[BNAppException.saveLogFile] ERROR CRÍTICO ❱ " + e.getMessage() + CMDColor.RESET);
        }
    }
}
