package forOlderJava.absurdityAppForJava.domain.item.service;

import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.item.service.request.RegisterItemCommand;
import forOlderJava.absurdityAppForJava.domain.item.service.response.ItemRedisDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCacheService itemCacheService;
    private final RedisCacheService redisCacheService;

    private static final String AVERAGE_RATE_CACHE_KEY = "averageRating::Item:";

    @Transactional
    public Long saveItem(RegisterItemCommand registerItemCommand) {
        Item item = Item.builder()
                .name(registerItemCommand.name())
                .price(registerItemCommand.price())
                .description(registerItemCommand.description())
                .quantity(registerItemCommand.quantity())
                .discount(registerItemCommand.discount())
                .maxBuyQuantity(registerItemCommand.maxBuyQuantity())
                .build();

        Item savedItem = itemRepository.save(item);
        itemCacheService.saveNewItem(ItemRedisDto.from(savedItem));
        return savedItem.getId();
    }
}
