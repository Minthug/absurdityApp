package forOlderJava.absurdityAppForJava.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

//    @Bean
//    public QuerydslBinderCustomizer<QuerydslBindings> querydslBinderCustomizer() {
//        return new QuerydslBinderCustomizer<QuerydslBindings>() {
//            @Override
//            public void customize(QuerydslBindings bindings, QuerydslBindings root) {
//
//            }
//        }
//    }
}
