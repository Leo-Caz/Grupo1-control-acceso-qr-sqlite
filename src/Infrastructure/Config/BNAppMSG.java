package Infrastructure.Config;

import javax.swing.JOptionPane;

public abstract class BNAppMSG {
    
    private BNAppMSG() {}

    /**
     * Muestra un mensaje de información simple
     */
    public static final void bnShow(String msg){
        JOptionPane.showMessageDialog(null, msg, "🤖 VERIFY", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error (Con icono de alerta o calavera)
     */
    public static final void bnShowError(String msg){
        JOptionPane.showMessageDialog(null, msg, "💀 VERIFY Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra una pregunta de SI / NO
     * Retorna: true si dice SI, false si dice NO
     */
    public static final boolean bnShowConfirmYesNo(String msg){
        int respuesta = JOptionPane.showConfirmDialog(null, msg, "⚔️ Confirmar Acción", JOptionPane.YES_NO_OPTION);
        return (respuesta == JOptionPane.YES_OPTION);
    }
}
