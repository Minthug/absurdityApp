package forOlderJava.absurdityAppForJava.domain.member;

import lombok.Getter;

import java.util.List;

@Getter
public enum MemberRole {
    ROLE_OLDER(Constants.ROLE_OLDER, List.of(Constants.ROLE_OLDER)),
    ROLE_YOUNGER(Constants.ROLE_YOUNGER, List.of(Constants.ROLE_YOUNGER)),
    ROLE_ADMIN(Constants.ROLE_ADMIN, List.of(Constants.ROLE_ADMIN, Constants.ROLE_YOUNGER, Constants.ROLE_OLDER));

    private final String value;
    private final List<String> authorities;

    MemberRole(String value, List<String> authorities) {
        this.value = value;
        this.authorities = authorities;
    }

    private static class Constants {
        private static final String ROLE_OLDER = "ROLE_OLDER";
        private static final String ROLE_YOUNGER = "ROLE_YOUNGER";
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
