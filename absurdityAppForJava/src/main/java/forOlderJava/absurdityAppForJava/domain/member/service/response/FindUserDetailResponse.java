package forOlderJava.absurdityAppForJava.domain.member.service.response;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.MemberGrade;
import forOlderJava.absurdityAppForJava.domain.member.MemberRole;

public record FindUserDetailResponse(Long memberId,
                                     String nickname,
                                     String email,
                                     String provider,
                                     String providerId,
                                     MemberRole memberRole,
                                     MemberGrade memberGrade) {

    public static FindUserDetailResponse from(final Member findMember) {
        return new FindUserDetailResponse(
                findMember.getId(),
                findMember.getNickname(),
                findMember.getEmail(),
                findMember.getProvider(),
                findMember.getProviderId(),
                findMember.getMemberRole(),
                findMember.getMemberGrade());
    }
}
