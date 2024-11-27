package forOlderJava.absurdityAppForJava.payment;

import forOlderJava.absurdityAppForJava.global.auth.jwt.dto.JwtAuthentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomerUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomerUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomerUser customerUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtAuthentication auth = new JwtAuthentication(
                customerUser.memberId(),
                customerUser.accessToken()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(auth, null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_OLDER")));

        context.setAuthentication(authentication);
        return context;
    }
}
