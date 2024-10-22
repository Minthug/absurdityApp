package forOlderJava.absurdityAppForJava.domain.member;

import forOlderJava.absurdityAppForJava.domain.member.exception.InvalidMemberException;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@Table(name = "members")
@Entity
public class Member extends BaseTimeEntity {

    private static final int NICKNAME_LENGTH = 200;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]{1,64}@([a-zA-Z0-9.-]{1,255})$");


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;
    @Column
    private String email;

    @Column(nullable = false)
    private String provider;
    @Column(nullable = false)
    private String providerId;

    @Column
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberGrade memberGrade;

    @Builder
    public Member(final String nickname, final String email, final String provider, final String providerId, final String location, final MemberRole memberRole, final MemberGrade memberGrade) {
        validateNickname(nickname);
        validateEmail(email);
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.location = location;
        this.memberRole = memberRole;
        this.memberGrade = memberGrade;
    }

    private void validateNickname(String nickname) {
        if (nickname.length() > NICKNAME_LENGTH) {
            throw new InvalidMemberException("사용할 수 없는 닉네임 입니다.");
        }
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidMemberException("사용할 수 없는 이메일 입니다");
        }
    }

    public boolean isSameMemberId(Long id) {
        return this.id.equals(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return true;
        }
        Member member = (Member) obj;
        return Objects.equals(getProvider(), member.getProvider()) && Objects.equals(getProviderId(), member.getProviderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProvider(), getProviderId());
    }

    public boolean isYounger() {
        return memberRole == MemberRole.ROLE_YOUNGER;
    }
}
