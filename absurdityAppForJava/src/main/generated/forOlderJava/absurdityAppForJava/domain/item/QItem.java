package forOlderJava.absurdityAppForJava.domain.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -958258448L;

    public static final QItem item = new QItem("item");

    public final forOlderJava.absurdityAppForJava.global.QBaseTimeEntity _super = new forOlderJava.absurdityAppForJava.global.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> maxBuyQuantity = createNumber("maxBuyQuantity", Integer.class);

    public final StringPath name = createString("name");

    public final ListPath<forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem, forOlderJava.absurdityAppForJava.domain.order.entity.QOrderItem> orderItems = this.<forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem, forOlderJava.absurdityAppForJava.domain.order.entity.QOrderItem>createList("orderItems", forOlderJava.absurdityAppForJava.domain.order.entity.OrderItem.class, forOlderJava.absurdityAppForJava.domain.order.entity.QOrderItem.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Double> rate = createNumber("rate", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QItem(String variable) {
        super(Item.class, forVariable(variable));
    }

    public QItem(Path<? extends Item> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItem(PathMetadata metadata) {
        super(Item.class, metadata);
    }

}

