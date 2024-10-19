package forOlderJava.absurdityAppForJava.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {

    CHECK("심부름 접수"),
    APPROVAL("심부름 승인"),
    OLDER_CANCEL_REQUEST("형/누나의 변덕"),
    OLDER_CANCEL("단순변심"),
    YOUNGER_CANCEL("반항"),
    COOKING("요리 중"),
    SHIPPING("가능 중"),
    COME_OUT("나오세요");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
