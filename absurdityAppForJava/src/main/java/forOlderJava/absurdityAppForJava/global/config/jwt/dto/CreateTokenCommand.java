package forOlderJava.absurdityAppForJava.global.config.jwt.dto;

import forOlderJava.absurdityAppForJava.domain.member.MemberRole;
import forOlderJava.absurdityAppForJava.domain.member.service.response.RegisterMemberResponse;

public record CreateTokenCommand(Long memberId, MemberRole memberRole) {

    public static CreateTokenCommand from(RegisterMemberResponse memberResponse) {
        return new CreateTokenCommand(memberResponse.memberId(), memberResponse.memberRole());
    }

    public static CreateTokenCommand of(final Long memberId, final MemberRole memberRole) {
        return new CreateTokenCommand(memberId, memberRole);
    }
}
