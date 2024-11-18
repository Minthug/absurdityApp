package forOlderJava.absurdityAppForJava.domain.item;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "item_cache", timeToLive = 3600)
public class ItemCache {

    @Id
    private String id;
    private String name;
    private Integer price;
    private Integer quantity;
    private Integer discount;
    private String description;
    private ItemSortType itemSortType;

}

