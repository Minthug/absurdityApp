package forOlderJava.absurdityAppForJava.global.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.Claims;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.CreateTokenCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {
    private static final String MEMBER_ID = "memberId";
    private static final String ROLE = "role";

    private final String issuer;
    private final String clientSecret;
    private final int expirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtTokenProvider(@Value("${jwt.issuer}") final String issuer,
                            @Value("${jwt.client-secret}") final String clientSecret,
                            @Value("${jwt.expiry-seconds}") final int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.Claims;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.CreateTokenCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

        @Slf4j
        @Component
        public class JwtTokenProvider implements TokenProvider {
            private static final String MEMBER_ID = "memberId";
            private static final String ROLE = "role";

            private final String issuer;
            private final String clientSecret;
            private final int expirySeconds;
            private final Algorithm algorithm;
            private final JWTVerifier jwtVerifier;

            public JwtTokenProvider(@Value("${jwt.issuer}") final String issuer,
                                    @Value("${jwt.client-secret}") final String clientSecret,
                                    @Value("${jwt.expiry-seconds}") final int expirySeconds) {
                this.issuer = issuer;
                this.clientSecret = clientSecret;
                this.expirySeconds = expirySeconds;
                this.algorithm = Algorithm.HMAC512(clientSecret);
                this.jwtVerifier = JWT.require(algorithm)
                        .withIssuer(issuer)
                        .build();
            }

            @Override
            public String createToken(CreateTokenCommand createTokenCommand) {
                return "";
            }

            @Override
            public Claims validateToken(String accessToken) {
                return null;
            }
        }

    }

    @Override
    public String createToken(CreateTokenCommand createTokenCommand) {
        return "";
    }

    @Override
    public Claims validateToken(String accessToken) {
        return null;
    }
}
