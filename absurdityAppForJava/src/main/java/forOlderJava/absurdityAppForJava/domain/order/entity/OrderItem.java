package forOlderJava.absurdityAppForJava.domain.order.entity;

import forOlderJava.absurdityAppForJava.domain.item.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public OrderItem(Item item, Integer quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public int calculateSubTotal() {
        return item.getPrice() * quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
