package forOlderJava.absurdityAppForJava.domain.younger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QErrand is a Querydsl query type for Errand
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QErrand extends EntityPathBase<Errand> {

    private static final long serialVersionUID = -1965957319L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QErrand errand = new QErrand("errand");

    public final forOlderJava.absurdityAppForJava.global.QBaseTimeEntity _super = new forOlderJava.absurdityAppForJava.global.QBaseTimeEntity(this);

    public final DateTimePath<java.time.LocalDateTime> arrivedAt = createDateTime("arrivedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> errandPrice = createNumber("errandPrice", Integer.class);

    public final EnumPath<ErrandStatus> errandStatus = createEnum("errandStatus", ErrandStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final forOlderJava.absurdityAppForJava.domain.order.entity.QOrder order;

    public final NumberPath<Integer> tip = createNumber("tip", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public final QYounger younger;

    public final StringPath youngerRequest = createString("youngerRequest");

    public QErrand(String variable) {
        this(Errand.class, forVariable(variable), INITS);
    }

    public QErrand(Path<? extends Errand> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QErrand(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QErrand(PathMetadata metadata, PathInits inits) {
        this(Errand.class, metadata, inits);
    }

    public QErrand(Class<? extends Errand> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new forOlderJava.absurdityAppForJava.domain.order.entity.QOrder(forProperty("order"), inits.get("order")) : null;
        this.younger = inits.isInitialized("younger") ? new QYounger(forProperty("younger")) : null;
    }

}

