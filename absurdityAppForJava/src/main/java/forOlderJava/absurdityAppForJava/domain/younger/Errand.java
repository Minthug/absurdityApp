package forOlderJava.absurdityAppForJava.domain.younger;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderStatus;
import forOlderJava.absurdityAppForJava.domain.younger.exception.AlreadyAssignedErrandException;
import forOlderJava.absurdityAppForJava.domain.younger.exception.InvalidErrandException;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import forOlderJava.absurdityAppForJava.global.auth.exception.UnAuthenticationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Errand extends BaseTimeEntity {

    private static final int LOCATION_LENGTH = 500;
    private static final int ZERO = 0;
    public static final String DELETED = "삭제됨";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youngerId")
    private Younger younger;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ErrandStatus errandStatus;

    @Column
    private LocalDateTime arrivedAt;

    @Version
    private Long version;

    @Column
    private String location;

    @Column
    private Integer errandPrice;

    @Column
    private String youngerRequest;

    @Column
    private Integer tip;

    @Column
    private Long memberId;

    @Builder
    public Errand(final Order order, final int estimateMinutes) {
        validateErrandStatus(order);
        validateEstimateMinutes(estimateMinutes);
        this.order = order;
        this.errandStatus = ErrandStatus.ACCEPTING_ERRAND;
        this.arrivedAt = LocalDateTime.now().plusMinutes(estimateMinutes);
        this.location = order.getOrderer().getLocation();
        this.errandPrice = order.getOrderInfo().getErrandPrice();
        this.youngerRequest = order.getYoungerRequest();
        this.tip = order.getTip();
        this.memberId = order.getMember().getId();
        order.updateOrderStatus(OrderStatus.SHIPPING);
    }

    private void validateErrandStatus(Order order) {
        if (!order.isPayed()) throw new InvalidErrandException("결제 완료된 주문 입니다");
    }

    private void validateEstimateMinutes(final int estimateMinutes) {
        if (estimateMinutes < ZERO) throw new InvalidErrandException("심부름 소요 시간은 음수일 수 없습니다");
    }

    public boolean isOwnByMember(final Member member) {
        return this.order.isOwnByMember(member);
    }

    public void startErrand(final int estimateMinutes) {
        validateEstimateMinutes(estimateMinutes);
        this.arrivedAt = LocalDateTime.now().plusMinutes(estimateMinutes);
        this.errandStatus = ErrandStatus.START_ERRAND;
    }

    public void checkAuthority(final Younger younger) {
        if (!this.younger.equals(younger)) {
            throw new UnAuthenticationException("권한이 없습니다");
        }
    }

    public void assignYounger(Younger younger) {
        checkAlreadyAssignedToYounger();
        this.younger = younger;
    }

    private void checkAlreadyAssignedToYounger() {
        if (Objects.nonNull(this.younger)) {
            throw new AlreadyAssignedErrandException("이미 완료된 심부름 입니다");
        }
    }

    public void completeErrand() {
        this.arrivedAt = LocalDateTime.now();
        this.errandStatus = ErrandStatus.COMPLETE;
    }

    public void deleteAboutMember() {
        this.order = null;
        this.location = DELETED;
    }
}
