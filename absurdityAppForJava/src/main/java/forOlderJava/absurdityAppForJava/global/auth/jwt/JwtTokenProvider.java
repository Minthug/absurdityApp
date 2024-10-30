package forOlderJava.absurdityAppForJava.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;
import forOlderJava.absurdityAppForJava.global.auth.exception.InvalidJwtException;
import forOlderJava.absurdityAppForJava.global.auth.jwt.dto.Claims;
import forOlderJava.absurdityAppForJava.global.auth.jwt.dto.CreateTokenCommand;
import forOlderJava.absurdityAppForJava.global.auth.jwt.dto.TokenPair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {
    private static final String MEMBER_ID = "memberId";
    private static final String ROLE = "role";
    private static final String TOKEN_TYPE = "tokenType";
    private static final String ACCESS_TOKEN = "ACCESS";
    private static final String REFRESH_TOKEN = "REFRESH";

    @Value("${spring.jwt.issuer}")
    private final String issuer;
    @Value("${spring.jwt.client-secret}")
    private final String clientSecret;

    private final int accessTokenExpirySeconds;
    private final int refreshTokenExpirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtTokenProvider(@Value("${spring.jwt.issuer}") final String issuer,
                            @Value("${spring.jwt.client-secret}") final String clientSecret,
                            @Value("${spring.jwt.access-expiry-seconds}") final int accessTokenExpirySeconds,
                            @Value("${spring.jwt.refresh-expiry-seconds}") final int refreshTokenExpirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    @Override
    public String createToken(final CreateTokenCommand createTokenCommand) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + accessTokenExpirySeconds * 1000L);
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim(MEMBER_ID, createTokenCommand.memberId())
                .withClaim(ROLE, createTokenCommand.memberRole().getValue())
                .sign(algorithm);
    }

    @Override
    public TokenPair createTokenPair(final CreateTokenCommand createTokenCommand) {
        String accessToken = createToken(createTokenCommand, ACCESS_TOKEN, accessTokenExpirySeconds);
        String refreshToken = createToken(createTokenCommand, REFRESH_TOKEN, refreshTokenExpirySeconds);
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public String refreshAccessToken(final String refreshToken) {
        try {
            Claims claims = validateToken(refreshToken);
            if (!REFRESH_TOKEN.equals(claims.tokenType())) {
                throw new InvalidJwtException("유효하지 않은 RefreshToken 입니다.");
            }
            MemberRole role = MemberRole.valueOf(claims.role());
            CreateTokenCommand command = new CreateTokenCommand(claims.memberId(), role);
            return createToken(command, ACCESS_TOKEN, accessTokenExpirySeconds);
        } catch (JWTVerificationException ex) {
            log.info("Refresh Token Verification Exception: {}", ex.getMessage());
            throw new InvalidJwtException("유효하지 않은 Refresh 토큰 입니다.");
        }
    }

    public String createToken(final CreateTokenCommand createTokenCommand, String tokenType, int expirySeconds) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirySeconds * 1000L);
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .withClaim(MEMBER_ID, createTokenCommand.memberId())
                .withClaim(ROLE, createTokenCommand.memberRole().getValue())
                .sign(algorithm);
    }

    @Override
    public Claims validateToken(final String token) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Long memberId = getMemberId(decodedJWT);
            String role = getRole(decodedJWT);
            List<String> authorities = MemberRole.valueOf(role).getAuthorities();
            String tokenType = getTokenType(decodedJWT);
            return new Claims(memberId, role, authorities, tokenType);
        } catch (AlgorithmMismatchException ex) {
            log.info("AlgorithmMismatchException: 토큰의 알고리즘이 유효하지 않습니다.");
        } catch (SignatureVerificationException ex) {
            log.info("SignatureVerificationException: 토큰 서명이 유효하지 않습니다.");
        } catch (TokenExpiredException ex) {
            log.info("TokenExpiredException: 토큰이 만료 되었습니다.");
        } catch (MissingClaimException ex) {
            log.info("MissingClaimException: 유효값이 클레임에 포함되지 않습니다.");
        } catch (JWTVerificationException ex) {
            log.info("JWTVerificationException: 유효하지 않는 토큰입니다.");
        }
        throw new InvalidJwtException("유효하지 않는 토큰입니다");
    }

    private String getTokenType(DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(TOKEN_TYPE);
        if (!claim.isNull()) {
            return claim.asString();
        }
        throw new MissingClaimException(TOKEN_TYPE);
    }

    private String getRole(DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(ROLE);
        if (!claim.isNull()) {
            return claim.asString();
        }
        throw new MissingClaimException(ROLE);
    }

    private Long getMemberId(final DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(MEMBER_ID);
        if (!claim.isNull()) {
            return claim.asLong();
        }
        throw new MissingClaimException(MEMBER_ID);
    }

    private List<String> getAuthorities(final DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(ROLE);
        if (!claim.isNull()) {
            String role = claim.asString();
            return MemberRole.valueOf(role).getAuthorities();
        }
        throw new MissingClaimException(ROLE);
    }
}
