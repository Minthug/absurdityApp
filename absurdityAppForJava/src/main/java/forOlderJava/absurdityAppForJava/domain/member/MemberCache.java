package forOlderJava.absurdityAppForJava.domain.member;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "member_cache", timeToLive = 7200)
public class MemberCache {

    @Id
    private String id;
    private String nickname;
    private String email;
    private MemberGrade memberGrade;

}
