package forOlderJava.absurdityAppForJava.domain.entity;

import forOlderJava.absurdityAppForJava.global.exception.CustomException;
import forOlderJava.absurdityAppForJava.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Embedded
    private OrderInfo orderInfo;

    @Embedded
    private Orderer orderer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderItem> orderItems;

    public void requestCancel() throws CustomException {
        if (orderInfo.getOrderStatus() == OrderStatus.SHIPPING ||
        orderInfo.getOrderStatus() == OrderStatus.COME_OUT ||
        orderInfo.getOrderStatus() == OrderStatus.COOKING) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderInfo.setOrderStatus(OrderStatus.OLDER_CANCEL_REQUEST);
    }
}
