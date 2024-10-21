package forOlderJava.absurdityAppForJava.global.config.jwt;

import forOlderJava.absurdityAppForJava.global.config.jwt.dto.Claims;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.CreateTokenCommand;

public interface TokenProvider {

    String createToken(final CreateTokenCommand createTokenCommand);
    Claims validateToken(final String accessToken);
}
