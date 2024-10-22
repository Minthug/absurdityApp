package forOlderJava.absurdityAppForJava.domain.member.service.request;

import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;
import lombok.Builder;

@Builder
public record RegisterUserCommand(String nickname,
                                  String email,
                                  String provider,
                                  String providerId,
                                  MemberRole memberRole,
                                  MemberGrade memberGrade) {

    public static RegisterUserCommand of(final String nickname,
                                         final String email,
                                         final String provider,
                                         final String providerId,
                                         final MemberRole memberRole,
                                         final MemberGrade memberGrade) {
        return RegisterUserCommand.builder()
                .nickname(nickname)
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .memberRole(memberRole)
                .memberGrade(memberGrade)
                .build();
    }
}
