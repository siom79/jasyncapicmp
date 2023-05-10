package jasyncapicmp;

public class JAsyncApiCmpTechnicalException extends RuntimeException {
    public JAsyncApiCmpTechnicalException(String msg) {
        super(msg);
    }

    public JAsyncApiCmpTechnicalException(String msg, Exception e) {
        super(msg, e);
    }
}
