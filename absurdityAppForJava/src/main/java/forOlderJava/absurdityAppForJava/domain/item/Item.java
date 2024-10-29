package forOlderJava.absurdityAppForJava.domain.item;

import forOlderJava.absurdityAppForJava.domain.item.exception.IllegalItemException;
import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;
import forOlderJava.absurdityAppForJava.global.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE item SET is_deleted = true WHERE item_id = ?")
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_Id")
    private Long id;

    @NotBlank(message = "상품명은 필수 항목입니다.")
    @Column(nullable = false)
    private String name;

    @Min(value = 0, message = "상품가격은 0원 이상이어야 합니다")
    @Column(nullable = false)
    private int price;

    private String description;
    private double rate;

    @Min(value = 0, message = "상품 재고수량은 0 이상이어야 합니다")
    @Column(nullable = false)
    private int quantity;

    @Min(value = 0, message = "상품 할인율은 0 이상이어야 합니다")
    @Column(nullable = false)
    private int discount;

    private boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Item(String name, int price, String description, double rate, int quantity, int discount) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.quantity = quantity;
        this.discount = discount;
    }

    public void decreaseQuantity(final int quantity) {
        this.quantity -= quantity;
    }

    public void increaseQuantity(final int quantity) {
        this.quantity += quantity;
    }

    public void updateItem(final String name, final int price, final int quantity,
                           final String description, final int discount) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.discount = discount;
    }

}
