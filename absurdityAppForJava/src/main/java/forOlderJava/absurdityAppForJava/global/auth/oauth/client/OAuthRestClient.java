package forOlderJava.absurdityAppForJava.global.auth.oauth.client;

import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.oauth.OAuthProvider;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.OAuthHttpMessage;
import forOlderJava.absurdityAppForJava.global.infrastructure.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthRestClient {
    private final ApiService apiService;
    private final OAuth2AuthorizedClientService authorizedClientService;


    public void callUnlinkOAuthUser(final FindUserDetailResponse userDetailResponse) {
        OAuthProvider oAuthProvider = OAuthProvider.getOAuthProvider(userDetailResponse.provider());
        OAuthHttpMessageProvider oAuthHttpMessageProvider = oAuthProvider.getOAuthHttpMessageProvider();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                userDetailResponse.provider(),
                userDetailResponse.providerId());

        refreshAccessTokenIfNotValid(userDetailResponse, authorizedClient);

        OAuthHttpMessage unlinkHttpMessage = oAuthHttpMessageProvider.createUnlinkUserRequest(userDetailResponse, authorizedClient);

        Map<String, Object> response = sendPostApiRequest(unlinkHttpMessage);
        log.info("회원의 연결이 종료되었습니다. 회원 ID={}", response);

        oAuthHttpMessageProvider.verifySuccessUnlinkUserRequest(response);
        authorizedClientService.removeAuthorizedClient(
                userDetailResponse.provider(),
                userDetailResponse.providerId());
    }


    private void refreshAccessTokenIfNotValid(FindUserDetailResponse userDetailResponse,
                                              OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        Instant expiresAt = oAuth2AuthorizedClient.getAccessToken().getExpiresAt();
        if (expiresAt.isBefore(Instant.now())) {
            callRefreshAccessToken(userDetailResponse);
        }
    }

    public void callRefreshAccessToken(final FindUserDetailResponse userDetailResponse) {
        OAuthProvider oAuthProvider = OAuthProvider.getOAuthProvider(userDetailResponse.provider());
        OAuthHttpMessageProvider oAuthHttpMessageProvider = oAuthProvider.getOAuthHttpMessageProvider();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
                userDetailResponse.provider(),
                userDetailResponse.providerId());
        OAuthHttpMessage refreshAccessTokenRequest = oAuthHttpMessageProvider.createRefreshAccessTokenRequest(oAuth2AuthorizedClient);

        Map response = sendPostApiRequest(refreshAccessTokenRequest);

        OAuth2AccessToken refreshedAccessToken = oAuthHttpMessageProvider.extractAccessToken(response);

        OAuth2RefreshToken refreshedRefreshToken = oAuthHttpMessageProvider.extractRefreshToken(response)
                .orElse(oAuth2AuthorizedClient.getRefreshToken());

        OAuth2AuthorizedClient updatedAuthorizedClinet = new OAuth2AuthorizedClient(
                oAuth2AuthorizedClient.getClientRegistration(),
                oAuth2AuthorizedClient.getPrincipalName(),
                refreshedAccessToken,
                refreshedRefreshToken);
        Authentication authenticationForTokenRefresh = getAuthenticationForTokenRefresh(updatedAuthorizedClinet);

        authorizedClientService.saveAuthorizedClient(
                updatedAuthorizedClinet,
                authenticationForTokenRefresh);
    }

    private Authentication getAuthenticationForTokenRefresh(OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        String principalName = oAuth2AuthorizedClient.getPrincipalName();
        return UsernamePasswordAuthenticationToken.authenticated(
                principalName, null, List.of());
    }

    private Map<String, Object> sendPostApiRequest(OAuthHttpMessage request) {
        return apiService.getResult(
                request.httpMessage(),
                request.uri(),
                Map.class,
                request.uriVariables());
    }
}
