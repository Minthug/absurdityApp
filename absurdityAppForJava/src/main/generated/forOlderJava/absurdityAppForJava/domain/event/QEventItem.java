package forOlderJava.absurdityAppForJava.domain.event;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventItem is a Querydsl query type for EventItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventItem extends EntityPathBase<EventItem> {

    private static final long serialVersionUID = -531752293L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventItem eventItem = new QEventItem("eventItem");

    public final QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final forOlderJava.absurdityAppForJava.domain.item.QItem item;

    public QEventItem(String variable) {
        this(EventItem.class, forVariable(variable), INITS);
    }

    public QEventItem(Path<? extends EventItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventItem(PathMetadata metadata, PathInits inits) {
        this(EventItem.class, metadata, inits);
    }

    public QEventItem(Class<? extends EventItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event")) : null;
        this.item = inits.isInitialized("item") ? new forOlderJava.absurdityAppForJava.domain.item.QItem(forProperty("item")) : null;
    }

}

