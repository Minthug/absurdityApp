package forOlderJava.absurdityAppForJava.global.config;

import forOlderJava.absurdityAppForJava.global.config.jwt.JwtAuthenticationProvider;
import forOlderJava.absurdityAppForJava.global.config.jwt.filter.JwtAuthenticationFilter;
import forOlderJava.absurdityAppForJava.global.config.oauth.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(JdbcOperations operations,
                                                                 ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(operations, clientRegistrationRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                           JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(requestPermitAll()).permitAll()
                        .requestMatchers(requestHasRoleOlder()).hasRole("OLDER")
                        .requestMatchers(requestHasRoleYoung()).hasRole("YOUNG")
                        .requestMatchers(requestHasRoleAdmin()).hasRole("ADMIN")
                        .anyRequest().denyAll())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(auth -> auth
                        .successHandler(oAuth2AuthenticationSuccessHandler))
                .addFilterAfter(new JwtAuthenticationFilter(jwtAuthenticationProvider), SecurityContextHolderFilter.class);
        return http.build();
    }

    private RequestMatcher[] requestHasRoleYoung() {
    }

    private RequestMatcher[] requestHasRoleOlder() {
    }

    private RequestMatcher[] requestPermitAll() {


    }
}
