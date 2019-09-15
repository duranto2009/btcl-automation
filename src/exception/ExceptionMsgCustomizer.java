package exception;

public class ExceptionMsgCustomizer {
    public static String getMessage(Exception ex) {
        StackTraceElement element = ex.getStackTrace()[0];
        return ex.getMessage() == null ? "Null Pointer Exception=>":ex.getMessage()
                +"=>" + element.getClassName() + "::" +element.getMethodName() + " at line "
                + element.getLineNumber();
    }
}
