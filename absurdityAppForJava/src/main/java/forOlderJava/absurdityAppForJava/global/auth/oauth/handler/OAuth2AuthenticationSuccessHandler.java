package forOlderJava.absurdityAppForJava.global.auth.oauth.handler;

import forOlderJava.absurdityAppForJava.global.auth.jwt.TokenProvider;
import forOlderJava.absurdityAppForJava.global.auth.jwt.dto.CreateTokenCommand;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws ServletException, IOException {

        if (authentication.getPrincipal() instanceof CustomOAuth2User customOAuth2User) {
            CreateTokenCommand createTokenCommand = CreateTokenCommand.from(customOAuth2User.getMemberResponse());
            String accessToken = tokenProvider.createToken(createTokenCommand);
            sendAccessToken(response, accessToken);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }

    private void sendAccessToken(final HttpServletResponse response, final String accessToken) throws IOException {
        response.setContentType("application/json");
        response.setContentLength(accessToken.getBytes().length);
        response.getWriter().write(accessToken);
    }
}
