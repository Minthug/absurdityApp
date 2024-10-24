package forOlderJava.absurdityAppForJava.domain.order.entity;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.aspectj.weaver.ast.Or;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orderer {
    private String nickname;
    private Long olderId;
    private String phoneNumber;
    private String location;

    @Builder
        public Orderer(String nickname, String phoneNumber, String location) {
        validateNickname(nickname);
        validatePhoneNumber(phoneNumber);
        validateLocation(location);
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public static Orderer from(Member member) {
        return Orderer.builder()
                .nickname(member.getNickname())
                .olderId(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .location(member.getLocation())
                .build();
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("주문자 이름은 필수 입니다.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("연락처는 기입은 필수 입니다");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("위치는 필수 기입 입니다.");
        }
    }
}
