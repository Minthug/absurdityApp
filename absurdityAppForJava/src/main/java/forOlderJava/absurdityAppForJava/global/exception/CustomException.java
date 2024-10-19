package forOlderJava.absurdityAppForJava.global.exception;

public class CustomException extends Exception {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
