package forOlderJava.absurdityAppForJava.global.auth.oauth.client;

import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.OAuthHttpMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static forOlderJava.absurdityAppForJava.global.auth.oauth.constant.OAuthConstant.ACCESS_TOKEN;

public class GoogleMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://oauth2.googleapis.com/revoke?token={access_token}";
    private static final String REFRESH_ACCESS_TOKEN_URI = "https://oauth2.googleapis.com/token";

    @Override
    public OAuthHttpMessage createUnlinkUserRequest(final FindUserDetailResponse userDetailResponse,
                                                    final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        String accessToken = getAccessToken(oAuth2AuthorizedClient);
        HttpEntity<MultiValueMap<String, String>> unlinkHttpMessage = createUnlinkUserMessage(accessToken);
        Map<String, String> unlinkUriVariables = createUnlinkUserUriVariables(accessToken);
        return new OAuthHttpMessage(UNLINK_URI, unlinkHttpMessage, unlinkUriVariables);
    }

    private Map<String, String> createUnlinkUserUriVariables(String accessToken) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(ACCESS_TOKEN, accessToken);
        return uriVariables;
    }

    private HttpEntity<MultiValueMap<String, String>> createUnlinkUserMessage(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", accessToken);
        return new HttpEntity<>(body, headers);
    }


    private String getAccessToken(final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    }

    @Override
    public void verifySuccessUnlinkUserRequest(Map<String, Object> unlinkResponse) {
    }

    @Override
    public OAuthHttpMessage createRefreshAccessTokenRequest(OAuth2AuthorizedClient authorizedClient) {
        return new OAuthHttpMessage(
                REFRESH_ACCESS_TOKEN_URI,
                createRefreshTokenMessage(authorizedClient),
                new HashMap<>());
    }

    private HttpEntity<MultiValueMap<String, String>> createRefreshTokenMessage(OAuth2AuthorizedClient authorizedClient) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", authorizedClient.getClientRegistration().getClientId());
        body.add("client_secret", authorizedClient.getClientRegistration().getClientSecret());
        body.add("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
        body.add("grant_type", "refresh_token");
        return new HttpEntity<>(body, headers);
    }

    @Override
    public OAuth2AccessToken extractAccessToken(Map response) {
        String accessToken = (String) response.get("access_token");
        Integer expiresIn = (Integer) response.get("expires_in");
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expiresIn);
        return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, now, expiresAt);
    }

    @Override
    public Optional<OAuth2RefreshToken> extractRefreshToken(Map response) {
        String refreshToken = (String) response.get("refresh_token");
        return Optional.ofNullable(refreshToken)
                .map(token -> new OAuth2RefreshToken(token, Instant.now()));
    }
}
