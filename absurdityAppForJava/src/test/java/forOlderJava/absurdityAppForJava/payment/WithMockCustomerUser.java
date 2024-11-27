package forOlderJava.absurdityAppForJava.payment;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomerUserSecurityContextFactory.class)
public @interface WithMockCustomerUser {

    long memberId() default 1L;
    String accessToken() default "test-access-token";
}
