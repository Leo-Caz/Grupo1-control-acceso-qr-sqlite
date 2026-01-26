//Framework por ahora 
package Framework;

/**
 * Excepción de aplicación para estandarizar errores.
 * Guarda información del lugar donde se originó el error.
 */
public class PatException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String className;
    private final String methodName;

    public PatException(String message) {
        super(message);
        this.className = null;
        this.methodName = null;
    }

    public PatException(String message, String className, String methodName) {
        super(message);
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getMessage() {
        String base = super.getMessage();
        if (className == null && methodName == null) {
            return base;
        }
        String cls = (className == null) ? "" : className;
        String mtd = (methodName == null) ? "" : methodName;
        return base + " [" + cls + (cls.isEmpty() || mtd.isEmpty() ? "" : ".") + mtd + "]";
    }
}

