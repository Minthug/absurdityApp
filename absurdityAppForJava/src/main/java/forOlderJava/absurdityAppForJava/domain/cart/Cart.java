package forOlderJava.absurdityAppForJava.domain.cart;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.global.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.isNull;

@Getter
@Entity
@Table(name = "cart")
@NoArgsConstructor
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Cart(final Member member) {
        validateMember(member);
        this.member = member;
    }

    private void validateMember(final Member member) {
        if (isNull(member)) {
            throw new NotFoundMemberException("Member가 존재 하지 않습니다.");
        }
    }


}
