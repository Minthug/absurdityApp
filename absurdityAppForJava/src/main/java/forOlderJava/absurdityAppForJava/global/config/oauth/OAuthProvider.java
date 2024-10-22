package forOlderJava.absurdityAppForJava.global.config.oauth;

import forOlderJava.absurdityAppForJava.global.config.exception.InvalidProviderException;
import forOlderJava.absurdityAppForJava.global.config.exception.OAuthUserInfoExtractException;
import forOlderJava.absurdityAppForJava.global.config.oauth.dto.OAuthHttpMessage;
import forOlderJava.absurdityAppForJava.global.config.oauth.dto.OAuthUserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum OAuthProvider {

    KAKAO("kakao", OAuthProvider::extractKakaoUserInfo, new KakaoMessageProvider()),
    NAVER("naver", OAuthProvider::extractNaverUserInfo, new NaverMessageProvider()),
    GOOGLE("google", OAuthProvider::extractGoogleUserInfo, new GoogleMessageProvider());

    private static final Map<String, OAuthProvider> PROVIDER = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(OAuthProvider::getName, Function.identity())));

    private final String name;
    private final Function<Map<String, Object>, OAuthUserInfo> extractUserInfo;
    private final OAuthHttpMessageProvider oAuthHttpMessageProvider;

    private static OAuthUserInfo extractNaverUserInfo(Map<String, Object> attributes) {
        try {
            Map<String, String> response = (Map<String, String>) attributes.get("response");
            String oAuthUserId = response.get("id");
            String nickname = response.get("nickname");
            String email = response.get("email");
            return new OAuthUserInfo(oAuthUserId, nickname, email);
        } catch (Exception e) {
            log.error("Failed to extract Naver user info: {}", e.getMessage());
            throw new OAuthUserInfoExtractException("네이버 사용자 정보 추출 실패");
        }
    }

    private static OAuthUserInfo extractKakaoUserInfo(Map<String, Object> attributes) {
        try {
            Map<String, String> properties = (Map<String, String>) attributes.get("properties");
            String oAuthUserId = String.valueOf(attributes.get("id"));
            String nickname = properties.get("nickname");
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            String email = String.valueOf(kakaoAccount.get("email"));
            return new OAuthUserInfo(oAuthUserId, nickname, email);
        } catch (Exception e) {
            log.error("Failed to extract Kakao user info: {}", e.getMessage());
            throw new OAuthUserInfoExtractException("카카오 사용자 정보 추출 실패");
        }
    }

    private static OAuthUserInfo extractGoogleUserInfo(Map<String, Object> attributes) {
        try {
            String oAUthUserId = (String) attributes.get("id");
            String nickname = (String) attributes.get("nickname"); // or "Given_name"
            String email = (String) attributes.get("email");

            return new OAuthUserInfo(oAUthUserId, nickname, email);
        } catch (Exception e) {
            log.error("Failed to extract Google user info: {}", e.getMessage());
            throw new OAuthUserInfoExtractException("구글 사용자 정보 추출 실패");
        }
    }

    public static OAuthProvider getOAuthProvider(final String provider) {
        OAuthProvider oAuthProvider = PROVIDER.get(provider);
        return Optional.ofNullable(oAuthProvider)
                .orElseThrow(() -> new InvalidProviderException("지원하지 않는 소셜 로그인 입니다"));
    }

    public OAuthUserInfo getOAuthUserInfo(final Map<String, Object> attributes) {
        return this.extractUserInfo.apply(attributes);
    }
}
