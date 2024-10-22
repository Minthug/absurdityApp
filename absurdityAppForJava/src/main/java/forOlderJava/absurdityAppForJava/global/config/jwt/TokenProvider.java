package forOlderJava.absurdityAppForJava.global.config.jwt;

import forOlderJava.absurdityAppForJava.global.config.jwt.dto.Claims;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.CreateTokenCommand;
import forOlderJava.absurdityAppForJava.global.config.jwt.dto.TokenPair;

public interface TokenProvider {


    /**
     *
     * @param createTokenCommand
     * @return
     */
    String createToken(final CreateTokenCommand createTokenCommand);

    /**
     * 새로운 액세스 토큰과 리프레쉬 토큰 쌍을 생성합니다.
     *
     * @param createTokenCommand 토큰 생성에 필요한 정보를 담은 커맨드 객체
     * @return 생성된 액세스 토큰과 리프레쉬 토큰 쌍
     */
    TokenPair createTokenPair(final CreateTokenCommand createTokenCommand);

    /**
     * 주어진 토큰 (액세스 or 리프레쉬)의 유효성을 검증합니다.
     *
     * @param token 검증할 토큰
     * @return 토큰에서 추출한 클레임 정보
     * @Throws InvalidJwtException 토큰이 유효하지 않는 경우
     */
    Claims validateToken(final String token);

    /**
     * 리프레쉬 토큰을 사용하여 새로운 액세스 토큰을 생성합니다.
     *
     * @param refreshToken 유효한 리프레쉬 토큰
     * @return 새로 생성된 액세스 토큰
     * @throws //InvalidJwtException 리프레시 토큰이 유효하지 않거나 만료된 경우
     */
    String refreshAccessToken(final String refreshToken);
}
