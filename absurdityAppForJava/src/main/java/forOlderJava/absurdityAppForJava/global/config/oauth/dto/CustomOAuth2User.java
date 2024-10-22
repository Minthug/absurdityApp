package forOlderJava.absurdityAppForJava.global.config.oauth.dto;

import forOlderJava.absurdityAppForJava.domain.member.service.response.RegisterMemberResponse;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final RegisterMemberResponse memberResponse;
    private final List<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(final RegisterMemberResponse memberResponse,
                            final Map<String, Object> attributes) {
        this.memberResponse = memberResponse;
        this.authorities = memberResponse.memberRole().getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttribute(String name) {
        return attributes;
    }

    @Override
    public String getName() {
        return memberResponse.providerId();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}

