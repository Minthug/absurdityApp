package forOlderJava.absurdityAppForJava.domain.item.service;

import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.ItemSortType;
import forOlderJava.absurdityAppForJava.domain.item.exception.NotFoundItemException;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import forOlderJava.absurdityAppForJava.domain.item.service.request.*;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindItemDetailResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindItemsResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindNewItemsResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.response.FindNewItemsResponse.FindNewItemResponse;
import forOlderJava.absurdityAppForJava.domain.item.service.response.ItemRedisDto;
import forOlderJava.absurdityAppForJava.domain.order.service.response.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCacheService itemCacheService;
    private final RedisCacheService redisCacheService;

    private static final String ORDER_COUNT_CACHE_KEY = "orderCount:Item";
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

    @Transactional(readOnly = true)
    public FindNewItemsResponse findNewItemsWithRedis(ItemSortType sortType) {
        List<ItemRedisDto> itemRedisDtos = itemCacheService.getNewItems();
        List<FindNewItemResponse> items = itemRedisDtos.stream()
                .map(item -> FindNewItemResponse.of(
                        item.itemId(),
                        item.name(),
                        item.price(),
                        item.discount(),
                        redisCacheService.getTotalNumberOfOrdersByMemberId(item.itemId(), ORDER_COUNT_CACHE_KEY + item.itemId()),
                        redisCacheService.getAverageRatingByItemId(item.itemId(), AVERAGE_RATE_CACHE_KEY + item.itemId())
                ))
                .toList();

        return FindNewItemsResponse.from(sortNewItems(items, sortType));
    }

    private List<FindNewItemResponse> sortNewItems(List<FindNewItemResponse> items, ItemSortType sortType) {
        List<FindNewItemResponse> sortedItems = new ArrayList<>(items);
        switch (sortType) {
            case LOWEST_AMOUNT -> sortedItems.sort(Comparator.comparingInt(FindNewItemResponse::price));
            case HIGHEST_AMOUNT -> sortedItems.sort(Comparator.comparingInt(FindNewItemResponse::price).reversed());
            case NEW -> sortedItems.sort(Comparator.comparingLong(FindNewItemResponse::itemId).reversed());
            case DISCOUNT -> sortedItems.sort(Comparator.comparingInt(FindNewItemResponse::discount).reversed());
            default -> sortedItems.sort(Comparator.comparingLong(FindNewItemResponse::orderCount).reversed());
        }
        return sortedItems;
    }

    @Transactional
    public void updateItem(UpdateItemCommand updateItemCommand) {

        Long itemId = updateItemCommand.itemId();

        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new NotFoundItemException("해당 Id의 아이템은 존재하지 않습니다"));
        item.updateItem(updateItemCommand.name(), updateItemCommand.price(),
                updateItemCommand.quantity(), updateItemCommand.description(),
                updateItemCommand.discount());
    }

    @Transactional
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    public FindItemsResponse findHotItems(FindHotItemsCommand findHotItemsCommand) {

        List<Item> items = itemRepository.findHotItemsOrderBy(findHotItemsCommand.lastIdx(),
                findHotItemsCommand.lastItemId(), findHotItemsCommand.itemSortType(),
                findHotItemsCommand.pageRequest());

        return FindItemsResponse.from(items);
    }

    @Transactional(readOnly = true)
    public List<Item> findItemsByIds(List<Long> itemIds) {
        List<Item> cachedItems = itemIds.stream()
                .map(itemCacheService::getItemInfo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::convertToItem)
                .collect(Collectors.toList());

        if (cachedItems.size() == itemIds.size()) {
            return cachedItems;
        }

        List<Long> uncachedItemIds = itemIds.stream()
                .filter(id -> !cachedItems.stream()
                        .map(Item::getId)
                        .collect(Collectors.toList())
                        .contains(id))
                .collect(Collectors.toList());

        List<Item> dbItems = itemRepository.findByItemIdIn(uncachedItemIds);

        dbItems.forEach(item -> itemCacheService.saveNewItem(ItemRedisDto.from(item)));

        List<Item> allItems = new ArrayList<>(cachedItems);
        allItems.addAll(dbItems);

        return itemIds.stream()
                .map(id -> allItems.stream()
                        .filter(item -> item.getId().equals(id))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Item convertToItem(ItemRedisDto itemRedisDto) {
        return Item.builder()
                .id(itemRedisDto.itemId())
                .name(itemRedisDto.name())
                .price(itemRedisDto.price())
                .discount(itemRedisDto.discount())
                .build();
    }
}
