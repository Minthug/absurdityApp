package forOlderJava.absurdityAppForJava.domain.order.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

    private Long orderId;
    private Long brotherId;
    private int errandPrice;
    private boolean delStatus;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
