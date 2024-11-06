package forOlderJava.absurdityAppForJava.domain.event.exception;

public abstract class EventException extends RuntimeException {
    public EventException(String message) {
        super(message);
    }
}
