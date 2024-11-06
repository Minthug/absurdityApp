package forOlderJava.absurdityAppForJava.domain.event.service;

import forOlderJava.absurdityAppForJava.domain.event.Event;
import forOlderJava.absurdityAppForJava.domain.event.EventItem;
import forOlderJava.absurdityAppForJava.domain.event.exception.DuplicateEventItemException;
import forOlderJava.absurdityAppForJava.domain.event.exception.NotFoundEventException;
import forOlderJava.absurdityAppForJava.domain.event.repository.EventItemRepository;
import forOlderJava.absurdityAppForJava.domain.event.repository.EventRepository;
import forOlderJava.absurdityAppForJava.domain.event.service.request.RegisterEventItemsCommand;
import forOlderJava.absurdityAppForJava.domain.item.Item;
import forOlderJava.absurdityAppForJava.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventItemService {

    private final EventItemRepository eventItemRepository;
    private final EventRepository eventRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long registerEventItems(RegisterEventItemsCommand registerEventItemsCommand) {
        Event event = eventRepository.findById(registerEventItemsCommand.eventId())
                .orElseThrow(() -> new NotFoundEventException("존재하지 않은 이벤트 입니다"));

        List<Item> items = itemRepository.findByItemIdIn(registerEventItemsCommand.items());

        List<Item> duplicatedItems = eventItemRepository.findDuplicatedItems(event, items);

        if (!duplicatedItems.isEmpty()) {
            List<Long> duplicatedItemIdList = duplicatedItems.stream()
                    .map(Item::getId).toList();
            throw new DuplicateEventItemException(duplicatedItemIdList.toString() + "해당 아이템은 이미 이벤트에 등록된 상태 입니다");
        }

        List<EventItem> eventItems = items.stream()
                .map(item -> new EventItem(event, item))
                .toList();

        eventItemRepository.saveAll(eventItems);
        return event.getId();
    }
}
