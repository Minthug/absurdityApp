package forOlderJava.absurdityAppForJava.domain.member.service.response;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;
import forOlderJava.absurdityAppForJava.domain.member.service.request.RegisterUserCommand;

public record RegisterUserResponse(Long memberId,
                                   String nickname,
                                   String providerId,
                                   MemberRole memberRole) {

    public static RegisterUserResponse from(final Member member) {
        return new RegisterUserResponse(
                member.getId(),
                member.getNickname(),
                member.getProviderId(),
                member.getMemberRole());
    }
}
