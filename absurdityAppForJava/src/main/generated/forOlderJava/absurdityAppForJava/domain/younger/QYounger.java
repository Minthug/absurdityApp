package forOlderJava.absurdityAppForJava.domain.younger;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QYounger is a Querydsl query type for Younger
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QYounger extends EntityPathBase<Younger> {

    private static final long serialVersionUID = -327666498L;

    public static final QYounger younger = new QYounger("younger");

    public final forOlderJava.absurdityAppForJava.global.QBaseTimeEntity _super = new forOlderJava.absurdityAppForJava.global.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final StringPath password = createString("password");

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final NumberPath<Integer> ratingCount = createNumber("ratingCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public QYounger(String variable) {
        super(Younger.class, forVariable(variable));
    }

    public QYounger(Path<? extends Younger> path) {
        super(path.getType(), path.getMetadata());
    }

    public QYounger(PathMetadata metadata) {
        super(Younger.class, metadata);
    }

}

