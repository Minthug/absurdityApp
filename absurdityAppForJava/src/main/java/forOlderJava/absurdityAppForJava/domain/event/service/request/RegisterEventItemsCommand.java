package forOlderJava.absurdityAppForJava.domain.event.service.request;

import forOlderJava.absurdityAppForJava.domain.item.Item;

import java.util.List;

public record RegisterEventItemsCommand(Long eventId, List<Long> items) {

    public static RegisterEventItemsCommand of(final Long eventId, final List<Long> items) {
        return new RegisterEventItemsCommand(eventId, items);
    }
}
