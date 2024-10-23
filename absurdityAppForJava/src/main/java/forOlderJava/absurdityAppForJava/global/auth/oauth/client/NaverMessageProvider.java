package forOlderJava.absurdityAppForJava.global.auth.oauth.client;

import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.exception.OAuthUnlinkFailureException;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.OAuthHttpMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static forOlderJava.absurdityAppForJava.global.auth.oauth.constant.OAuthConstant.*;

public class NaverMessageProvider implements OAuthHttpMessageProvider {

    private static final String UNLINK_URI = "https://nid.naver.com/oauth2.0/token?"
            + "client_id={client_id}&client_secret={client_secret}&access_token={access_token}&"
            + "grant_type={grant_type}&service_provider={service_provider}";

    private static final String REFRESH_ACCESS_TOKEN_URI = "https://nid.naver.com/oauth2.0/token?"
            + "grant_type=refresh_token&client_id={client_id}&"
            + "client_secret={client_secret}&refresh_token={refresh_token}";

    @Override
    public OAuthHttpMessage createUnlinkUserRequest(final FindUserDetailResponse userDetailResponse,
                                                    final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        String accessToken = getAccessToken(oAuth2AuthorizedClient);
        HttpEntity<MultiValueMap<String, String>> unlinkHttpMessage = createUnlinkUserMessage();
        Map<String, String> unlinkUriVariables = createUnlinkUserUriVariables(
                oAuth2AuthorizedClient.getClientRegistration(), accessToken);
        return new OAuthHttpMessage(UNLINK_URI, unlinkHttpMessage, unlinkUriVariables);
    }

    private Map<String, String> createUnlinkUserUriVariables(final ClientRegistration clientRegistration,
                                                             final String accessToken) {

        Map<String, String> uriVariables =  new HashMap<>();
        uriVariables.put(CLIENT_ID, clientRegistration.getClientId());
        uriVariables.put(CLIENT_SECRET, clientRegistration.getClientSecret());
        uriVariables.put(ACCESS_TOKEN, accessToken);
        uriVariables.put(GRANT_TYPE, DELETE);
        uriVariables.put(SERVICE_PROVIDER, NAVER);
        return uriVariables;

    }

    private HttpEntity<MultiValueMap<String, String>> createUnlinkUserMessage() {
        HttpHeaders headers = createHeader();
        return new HttpEntity<>(headers);
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    private String getAccessToken(final OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    }

    @Override
    public void verifySuccessUnlinkUserRequest(Map<String, Object> unlinkResponse) {
        Optional.ofNullable(unlinkResponse.get(RESULT))
                .filter(result -> result.equals(SUCCESS))
                .orElseThrow(() -> new OAuthUnlinkFailureException("소셜 로그인 연동 해제가 실패하였습니다."));
    }

    @Override
    public OAuthHttpMessage createRefreshAccessTokenRequest(OAuth2AuthorizedClient authorizedClient) {
        return new OAuthHttpMessage(
                REFRESH_ACCESS_TOKEN_URI,
                createEmptyMessage(),
                createRefreshAccessTokenUriVariables(authorizedClient));
    }

    private Map<String, String> createRefreshAccessTokenUriVariables(OAuth2AuthorizedClient authorizedClient) {
        Map<String, String> variables = new HashMap<>();
        variables.put(CLIENT_ID, authorizedClient.getClientRegistration().getClientId());
        variables.put(CLIENT_SECRET, authorizedClient.getClientRegistration().getClientSecret());
        variables.put(REFRESH_TOKEN, authorizedClient.getRefreshToken().getTokenValue());
        variables.put(GRANT_TYPE, REFRESH_TOKEN);
        return variables;
    }

    private HttpEntity<MultiValueMap<String, String>> createEmptyMessage() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        return new HttpEntity<>(map, map);
    }

    @Override
    public OAuth2AccessToken extractAccessToken(Map response) {
        String accessToken = (String) response.get(ACCESS_TOKEN);
        String expiresInSeconds = (String) response.get(EXPIRES_IN);
        Instant now = Instant.now();
        Instant expiresIn = now.plusSeconds(Long.parseLong(expiresInSeconds));
        return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, now, expiresIn);
    }

    @Override
    public Optional<OAuth2RefreshToken> extractRefreshToken(Map response) {
        String refreshToken = (String) response.get(REFRESH_TOKEN);
        return Optional.ofNullable(refreshToken)
                .map(token -> new OAuth2RefreshToken(token, Instant.now()));
    }
}
