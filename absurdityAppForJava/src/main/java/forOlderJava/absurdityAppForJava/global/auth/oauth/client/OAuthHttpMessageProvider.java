package forOlderJava.absurdityAppForJava.global.auth.oauth.client;

import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.OAuthHttpMessage;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.util.Map;
import java.util.Optional;

public interface OAuthHttpMessageProvider {

    OAuthHttpMessage createUnlinkUserRequest(final FindUserDetailResponse userDetailResponse,
                                             final OAuth2AuthorizedClient oAuth2AuthorizedClient);

    void verifySuccessUnlinkUserRequest(Map<String, Object> unlinkResponse);

    OAuthHttpMessage createRefreshAccessTokenRequest(OAuth2AuthorizedClient authorizedClient);

    OAuth2AccessToken extractAccessToken(Map response);

    Optional<OAuth2RefreshToken> extractRefreshToken(Map response);

}
