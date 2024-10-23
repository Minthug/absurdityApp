package forOlderJava.absurdityAppForJava.global.auth.oauth.client;

import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.exception.OAuthUnlinkFailureException;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.OAuthHttpMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static forOlderJava.absurdityAppForJava.global.auth.oauth.constant.OAuthConstant.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


public class KakaoMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://kapi.kakao.com/v1/user/unlink";
    private static final String ACCESS_TOKEN_REFRESH_URI = "https://kauth.kakao.com/oauth/token";

    @Override
    public OAuthHttpMessage createUnlinkUserRequest(final FindUserDetailResponse userDetailResponse,
                                                    final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        String accessToken = getAccessToken(oAuth2AuthorizedClient);
        HttpEntity<MultiValueMap<String, String>> unlinkHttpMessage = createUnlinkUserMessage(userDetailResponse, accessToken);
        return new OAuthHttpMessage(UNLINK_URI, unlinkHttpMessage, new HashMap<>());
    }

    private String getAccessToken(final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    }

    private HttpEntity<MultiValueMap<String, String>> createUnlinkUserMessage(final FindUserDetailResponse userDetailResponse,
                                                                              final String accessToken) {
        HttpHeaders headers = createHeader(accessToken);
        MultiValueMap<String, String> multiValueMap = createUnlinkUserMessageBody(userDetailResponse);
        return new HttpEntity<>(multiValueMap, headers);

    }

    private MultiValueMap<String, String> createUnlinkUserMessageBody(final FindUserDetailResponse userDetailResponse) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(TARGET_ID_TYPE, USER_ID);
        multiValueMap.add(TARGET_ID, String.valueOf(userDetailResponse.providerId()));
        return multiValueMap;
    }

    private HttpHeaders createHeader(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.set(HttpHeaders.AUTHORIZATION, MessageFormat.format("Bearer: {0}", accessToken));
        return headers;
    }

    @Override
    public void verifySuccessUnlinkUserRequest(Map<String, Object> unlinkResponse) {
        Optional.ofNullable(unlinkResponse.get(ID))
                .orElseThrow(() -> new OAuthUnlinkFailureException("소셜 로그인 연동이 실패했습니다"));
    }

    @Override
    public OAuthHttpMessage createRefreshAccessTokenRequest(OAuth2AuthorizedClient authorizedClient) {
        return new OAuthHttpMessage(ACCESS_TOKEN_REFRESH_URI,
                createRefreshAccessTokenMessage(authorizedClient),
                new HashMap<>());
    }

    private HttpEntity<MultiValueMap<String, String>> createRefreshAccessTokenMessage(OAuth2AuthorizedClient authorizedClient) {
        return new HttpEntity<>(
                createRefreshAccessTokenMessageBody(authorizedClient),
                createRefreshAccessTokenMessageHeader());
    }

    private MultiValueMap<String, String> createRefreshAccessTokenMessageHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED_CHARSET_UTF_8);
        return headers;
    }

    private MultiValueMap<String, String> createRefreshAccessTokenMessageBody(OAuth2AuthorizedClient authorizedClient) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(GRANT_TYPE, REFRESH_TOKEN);
        params.add(CLIENT_ID, authorizedClient.getClientRegistration().getClientId());
        params.add(REFRESH_TOKEN, authorizedClient.getRefreshToken().getTokenValue());
        params.add(CLIENT_SECRET, authorizedClient.getClientRegistration().getClientSecret());
        return params;
    }

    @Override
    public OAuth2AccessToken extractAccessToken(Map response) {
        String accessToken = (String) response.get(ACCESS_TOKEN);
        Instant now = Instant.now();
        Integer expiresInSeconds = (Integer) response.get(EXPIRES_IN);
        Instant expiresIn = now.plusSeconds(expiresInSeconds);
        return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, now, expiresIn);
    }

    @Override
    public Optional<OAuth2RefreshToken> extractRefreshToken(Map response) {
        String refreshToken = (String) response.get(REFRESH_TOKEN);
        return Optional.ofNullable(refreshToken)
                .map(token -> new OAuth2RefreshToken(token, Instant.now()));
    }
}
