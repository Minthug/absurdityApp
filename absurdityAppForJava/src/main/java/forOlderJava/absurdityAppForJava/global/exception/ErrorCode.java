package forOlderJava.absurdityAppForJava.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    ORDER_NOT_FOUND("Can not Cancel the Order");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
