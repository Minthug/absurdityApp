package forOlderJava.absurdityAppForJava.domain.payment;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @Setter
    private String paymentKey;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Payment(Member member, Order order, PaymentStatus paymentStatus) {
        this.member = member;
        this.order = order;
        this.paymentStatus = paymentStatus;
    }

    public void changeStatus(final PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isMisMatchPrice(final int amount) {
        return this.order.isMisMatchPrice(amount);
    }

    public boolean isMisMatchPrice(final PaymentStatus paymentStatus) {
        return this.paymentStatus != paymentStatus;
    }
}
