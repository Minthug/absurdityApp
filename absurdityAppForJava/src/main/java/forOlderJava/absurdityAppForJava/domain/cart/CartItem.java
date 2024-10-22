package forOlderJava.absurdityAppForJava.domain.cart;

import forOlderJava.absurdityAppForJava.domain.cart.exception.InvalidCartItemException;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.isNull;


@Getter
@Entity
@Table(name = "cart_item")
@NoArgsConstructor
public class CartItem extends BaseTimeEntity {

    private static final int MIN_QUANTITY = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean isChecked;

    @Builder
    public CartItem(final Cart cart, final Item item, final int quantity) {
        this.cart = cart;
        this.item = item;
        this.quantity = quantity;
        this.isChecked = true;
    }

    public void validateCart(final Cart cart) {
        if (isNull(cart)) {
            throw new InvalidCartItemException("Cart가 존재 하지 않습니다.");
        }
    }

    public void validateItem(final Item item) {
        if (isNull(item)) {
            throw new InvalidCartItemException("Item가 존재 하지 않습니다.");
        }
    }

    public void validateQuantity(final int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidCartItemException("수량은 음수가 될수 없습니다");
        }
    }

    public void changeQuantity(final int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }
}
