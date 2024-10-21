package forOlderJava.absurdityAppForJava.global.config.jwt.dto;

import java.util.List;

public record Claims(Long memberId, String role, List<String> authorities, String tokenType) { }
