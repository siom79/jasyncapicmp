package jasyncapicmp;

public class JAsyncApiCmpUserException extends RuntimeException {
    public JAsyncApiCmpUserException(String msg) {
        super(msg);
    }

    public JAsyncApiCmpUserException(String msg, Exception e) {
        super(msg, e);
    }
}
