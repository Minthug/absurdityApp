package forOlderJava.absurdityAppForJava.domain.item.service;

import forOlderJava.absurdityAppForJava.domain.event.service.EventRedisDto;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.item.service.response.ItemRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ItemCacheService {

    private final RedisTemplate<String, ItemRedisDto> redisTemplate;
    private static final String ITEM_INFO_PREFIX = "item:info:";
    private static final String NEW_PRODUCTS_KEY = "new_products";
    private final ItemRepository itemRepository;


    public void deleteItemCache(Long itemId) {
        String key = ITEM_INFO_PREFIX + itemId;
        redisTemplate.delete(key);
    }

    public void cacheItemInfo(ItemRedisDto itemRedisDto) {
        redisTemplate.opsForValue().set(
                ITEM_INFO_PREFIX + itemRedisDto.itemId(),
                itemRedisDto,
                Duration.ofHours(24)
        );
    }

    public Optional<ItemRedisDto> getItemInfo(Long itemId) {
        String key = ITEM_INFO_PREFIX + itemId;
        ItemRedisDto redisDto = redisTemplate.opsForValue().get(key);

        if (redisDto != null) {
            return Optional.of(redisDto);
        }

        return itemRepository.findByItemId(itemId)
                .map(item -> {
                    ItemRedisDto itemRedisDto = ItemRedisDto.from(item);
                    cacheItemInfo(itemRedisDto);
                    return itemRedisDto;
                });
    }

    public void saveNewItem(final ItemRedisDto itemRedisDto) {
        redisTemplate.opsForList().rightPush(NEW_PRODUCTS_KEY, itemRedisDto);
    }

    public List<ItemRedisDto> getNewItems() {
        ListOperations<String, ItemRedisDto> listOperations = redisTemplate.opsForList();
        Long itemCount = listOperations.size(NEW_PRODUCTS_KEY);
        if (itemCount == null || itemCount == 0) {
            return null;
        }
        return listOperations.range(NEW_PRODUCTS_KEY, 0, -1);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldProducts() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minus(2, ChronoUnit.WEEKS);

        ListOperations<String, ItemRedisDto> items = redisTemplate.opsForList();
        Long itemCount = items.size(NEW_PRODUCTS_KEY);

        if (itemCount == null || itemCount == 0) {
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            ItemRedisDto item = items.index(NEW_PRODUCTS_KEY, i);
            if (item != null && item.createdAt().isBefore(twoWeeksAgo)) {
                items.remove(NEW_PRODUCTS_KEY,1 , item);
                i--;
            }
        }
    }
}

