package forOlderJava.absurdityAppForJava.global.config.oauth.client;

import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.config.oauth.OAuthProvider;
import forOlderJava.absurdityAppForJava.global.config.oauth.dto.OAuthHttpMessage;
import forOlderJava.absurdityAppForJava.global.infrastructure.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

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

        refreshAccessTokenIfNotValid(userDetailResponse, oAuth2AuthorizedClient);

        OAuthHttpMessage unlinkHttpMessage = oAuthHttpMessageProvider.createUnlinkUserRequest(userDetailResponse, oAuth2AuthorizedClient);

        Map<String, Object> response = sendPostApiRequest(unlinkHttpMessage);
        log.info("회원의 연결이 종료되었습니다. 회원 ID={}", response);

        oAuthHttpMessageProvider.verifySuccessUnlinkUserRequest(response);
        authorizedClientService.removeAuthorizedClient(
                userDetailResponse.provider(),
                userDetailResponse.providerId());
    }


    private void refreshAccessTokenIfNotValid(FindUserDetailResponse userDetailResponse,
                                              OAuth2AuthorizedClient oAuth2AuthorizedClient) {

    }

    public void callRefreshAccessToken(final FindUserDetailResponse userDetailResponse) {

        return;
    }

    private Authentication getAuthenticationForTokenRefresh(OAuth2AuthorizedClient oAuth2AuthorizedClient) {

        return
    }

    private Map<String, Object> sendPostApiRequest(OAuthHttpMessage unlinkHttpMessage) {
        return null;
    }
}
