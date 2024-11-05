package forOlderJava.absurdityAppForJava.global.auth.oauth.service;

import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;
import forOlderJava.absurdityAppForJava.domain.member.service.MemberService;
import forOlderJava.absurdityAppForJava.domain.member.service.response.RegisterMemberResponse;
import forOlderJava.absurdityAppForJava.domain.member.service.request.RegisterUserCommand;
import forOlderJava.absurdityAppForJava.global.auth.oauth.OAuthProvider;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.CustomOAuth2User;
import forOlderJava.absurdityAppForJava.global.auth.oauth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthUserInfo oAuthUserInfo = OAuthProvider.getOAuthProvider(registrationId)
                .getOAuthUserInfo(attributes);

        RegisterUserCommand registryUserCommand = RegisterUserCommand.of(
                oAuthUserInfo.nickname(),
                oAuthUserInfo.email(),
                registrationId,
                oAuthUserInfo.oAuthUserId(),
                MemberRole.ROLE_OLDER,
                MemberGrade.NORMAL);
        RegisterMemberResponse memberResponse = memberService.getOrRegisterMember(registryUserCommand);
        return new CustomOAuth2User(memberResponse, attributes);
    }
}
