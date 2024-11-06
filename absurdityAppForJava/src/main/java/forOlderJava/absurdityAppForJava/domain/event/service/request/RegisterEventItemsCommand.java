package forOlderJava.absurdityAppForJava.domain.event.service.request;

import forOlderJava.absurdityAppForJava.domain.item.Item;

import java.util.List;

public record RegisterEventItemsCommand(Long eventId, List<Item> items) {

    public static RegisterEventItemsCommand of(final Long eventId, final List<Item> items) {
        return new RegisterEventItemsCommand(eventId, items);
    }
}
