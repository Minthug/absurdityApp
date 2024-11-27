package forOlderJava.absurdityAppForJava.domain.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderInfo is a Querydsl query type for OrderInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QOrderInfo extends BeanPath<OrderInfo> {

    private static final long serialVersionUID = -1363399473L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderInfo orderInfo = new QOrderInfo("orderInfo");

    public final BooleanPath delStatus = createBoolean("delStatus");

    public final NumberPath<Integer> errandPrice = createNumber("errandPrice", Integer.class);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final EnumPath<OrderStatus> orderStatus = createEnum("orderStatus", OrderStatus.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final forOlderJava.absurdityAppForJava.domain.coupon.QUserCoupon userCoupon;

    public final NumberPath<Long> youngerId = createNumber("youngerId", Long.class);

    public QOrderInfo(String variable) {
        this(OrderInfo.class, forVariable(variable), INITS);
    }

    public QOrderInfo(Path<? extends OrderInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderInfo(PathMetadata metadata, PathInits inits) {
        this(OrderInfo.class, metadata, inits);
    }

    public QOrderInfo(Class<? extends OrderInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userCoupon = inits.isInitialized("userCoupon") ? new forOlderJava.absurdityAppForJava.domain.coupon.QUserCoupon(forProperty("userCoupon"), inits.get("userCoupon")) : null;
    }

}

