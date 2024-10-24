package forOlderJava.absurdityAppForJava.global.auth.controller;

import forOlderJava.absurdityAppForJava.global.auth.exception.*;
import forOlderJava.absurdityAppForJava.global.util.ErrorTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorTemplate> authExHandler(AuthException e) {
        return ResponseEntity.badRequest()
                .body(ErrorTemplate.of(e.getMessage()));
    }

    @ExceptionHandler({OAuthUnlinkFailureException.class, UnAuthenticationException.class, InvalidJwtException.class})
    public ResponseEntity<ErrorTemplate> authenticationFailExHandler(AuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorTemplate.of(e.getMessage()));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorTemplate> duplicateUsernameExHandler(AuthException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorTemplate.of(e.getMessage()));
    }
}
