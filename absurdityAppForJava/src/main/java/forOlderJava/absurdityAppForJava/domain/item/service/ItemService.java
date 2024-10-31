package forOlderJava.absurdityAppForJava.domain.item.service;

import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.exception.NotFoundItemException;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.item.service.request.FindItemDetailCommand;
import forOlderJava.absurdityAppForJava.domain.item.service.request.FindNewItemsCommand;
import forOlderJava.absurdityAppForJava.domain.item.service.request.RegisterItemCommand;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindItemDetailResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindItemsResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindNewItemsResponse;
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

    @Transactional(readOnly = true)
    public FindItemDetailResponse findItemDetail(FindItemDetailCommand findItemDetailCommand) {

        Item item = itemRepository.findByItemId(findItemDetailCommand.itemId())
                .orElseThrow(() -> new NotFoundItemException("존재하지 않은 상품 입니다"));

        return FindItemDetailResponse.of(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getQuantity(),
                item.getRate(),
                item.getDiscount(),
                item.getMaxBuyQuantity());
    }

    @Transactional(readOnly = true)
    public FindItemsResponse findNewItems(FindNewItemsCommand findNewItemsCommand) {
        return FindItemsResponse.from(
                itemRepository.findNewItemsOrderBy(findNewItemsCommand.lastIdx(),
                        findNewItemsCommand.lastItemId(), findNewItemsCommand.itemSortType(),
                        findNewItemsCommand.pageRequest()));

    }
}
