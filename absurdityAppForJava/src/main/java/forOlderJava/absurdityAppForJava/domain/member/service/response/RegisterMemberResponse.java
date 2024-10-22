package forOlderJava.absurdityAppForJava.domain.member.service.response;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;

public record RegisterMemberResponse(Long memberId, String nickname, String providerId, MemberRole memberRole) {

    public static RegisterMemberResponse from(final Member member) {
        return new RegisterMemberResponse(
                member.getId(),
                member.getNickname(),
                member.getProviderId(),
                member.getMemberRole()
        );
    }
}
