package forOlderJava.absurdityAppForJava.global.advice;

import com.google.api.gax.rpc.UnauthenticatedException;
import forOlderJava.absurdityAppForJava.domain.order.exception.InvalidOrderStatusException;
import forOlderJava.absurdityAppForJava.domain.order.exception.OrderException;
import forOlderJava.absurdityAppForJava.domain.order.exception.OrderNotFoundException;
import forOlderJava.absurdityAppForJava.global.auth.exception.AuthException;
import forOlderJava.absurdityAppForJava.global.auth.exception.InvalidJwtException;
import forOlderJava.absurdityAppForJava.global.auth.exception.OAuthUnlinkFailureException;
import forOlderJava.absurdityAppForJava.global.util.ErrorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.List;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    public GlobalExceptionHandler(View error) {
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorTemplate> handlerException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorTemplate> handleValidationException(
            MethodArgumentNotValidException exception) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getDefaultMessage()).append("\n");
        }
        log.error(errorMessage.toString(), exception);
        return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ErrorTemplate> handleRequestParameterException(MissingServletRequestParameterException exception) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "요청 파라미터가 누락되었습니다");
    }

    @ExceptionHandler({AuthException.class,
            OAuthUnlinkFailureException.class,
            UnauthenticatedException.class,
            InvalidJwtException.class})
    ResponseEntity<ErrorTemplate> handleAuthenticationException(AuthException e) {
        log.error("Authentication error: {}", e.getMessage());
        return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({InvalidOrderStatusException.class, OrderNotFoundException.class})
    ResponseEntity<ErrorTemplate> handleOrderException(OrderException e) {
        log.error("Order error: {}", e.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ErrorTemplate> createErrorResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(ErrorTemplate.of(message));
    }


}
