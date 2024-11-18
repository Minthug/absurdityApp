package forOlderJava.absurdityAppForJava.domain.member.service.response;

import forOlderJava.absurdityAppForJava.domain.member.Member;

import java.time.LocalDateTime;

public record MemberRedisDto(Long memberId, String nickname, String email, LocalDateTime lastActiveAt) {

    public static MemberRedisDto from(final Member member) {
        return new MemberRedisDto(member.getId(), member.getNickname(), member.getEmail(), member.getUpdatedAt());
    }
}
