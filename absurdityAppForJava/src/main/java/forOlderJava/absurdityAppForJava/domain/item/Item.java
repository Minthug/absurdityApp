package forOlderJava.absurdityAppForJava.domain.item;

import forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem;
import forOlderJava.absurdityAppForJava.global.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE item SET is_deleted = true WHERE item_id = ?")
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_Id")
    private Long id;

    private String name;

    private int price;

    private String description;

    private double rate;

    private int quantity;

    private int discount;

    private boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();
}
