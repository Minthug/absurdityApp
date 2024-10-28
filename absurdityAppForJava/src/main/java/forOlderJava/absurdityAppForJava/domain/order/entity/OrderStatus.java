package forOlderJava.absurdityAppForJava.domain.order.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public enum OrderStatus {

    CHECK("심부름 접수"),
    APPROVAL("심부름 승인"),
    OLDER_CANCEL_REQUEST("형/누나의 변덕"),
    OLDER_CANCEL("단순변심"),
    YOUNGER_CANCEL("반항"),
    COOKING("요리 중"),
    SHIPPING("가능 중"),
    COME_OUT("나오세요"),
    PAYING("결제"),
    PENDING("보류 중");

    private final String value;
    private final List<OrderStatus> nextStatus;

    OrderStatus(String value) {
        this.value = value;
        this.nextStatus = new ArrayList<>();
    }

    // 정적 블록에서 상태 전이 규칙 정의
    static {
        CHECK.nextStatus.addAll(Arrays.asList(APPROVAL, OLDER_CANCEL_REQUEST, OLDER_CANCEL));
        APPROVAL.nextStatus.addAll(Arrays.asList(COOKING, OLDER_CANCEL_REQUEST, YOUNGER_CANCEL));
        OLDER_CANCEL_REQUEST.nextStatus.addAll(Arrays.asList(OLDER_CANCEL));
        COOKING.nextStatus.addAll(Arrays.asList(SHIPPING));
        SHIPPING.nextStatus.addAll(Arrays.asList(COME_OUT));
        PAYING.nextStatus.addAll(Arrays.asList(PENDING, CHECK));  // 결제 상태 전이 추가
        PENDING.nextStatus.addAll(Arrays.asList(APPROVAL));// 결제 완료 후 상태 전이
    }

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return this.nextStatus.contains(nextStatus);
    }

    // 현재 상태에서 가능한 다음 상태들 조회
    public List<OrderStatus> getNextStatuses() {
        return Collections.unmodifiableList(nextStatus);
    }

    // 상태 변경 가능 여부 체크 (추가적인 유틸리티 메서드)
    public boolean isModifiable() {
        return this == CHECK || this == APPROVAL;
    }

    // 취소 가능 상태인지 체크
    public boolean isCancelable() {
        return this != SHIPPING && this != COME_OUT && this != COOKING;
    }

    public boolean isPayable() {
        return this == CHECK;
    }

    public boolean isPaying() {
        return this == PAYING;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isCompleted() {
        return this == APPROVAL || this == COOKING ||
                this == SHIPPING || this == COME_OUT;
    }

    public boolean isPaymentProcessing() {
        return this == PAYING || this == PENDING;
    }

    public String getInvalidTransitionMessage(OrderStatus targetStatus) {
        return String.format("잘못된 주문 상태 변경입니다: %s(%s) -> %s(%s)",
                this, this.value, targetStatus, targetStatus.getValue());
    }
}

