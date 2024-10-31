package forOlderJava.absurdityAppForJava.domain.item.service.response;

import forOlderJava.absurdityAppForJava.domain.item.Item;

import java.util.List;
import java.util.stream.Collectors;

public record FindItemsResponse(List<FindItemResponse> items) {

    public static FindItemsResponse from(final List<Item> items) {
        List<FindItemResponse> findItemResponses = items.stream()
                .map(FindItemResponse::from)
                .collect(Collectors.toList());
        return new FindItemsResponse(findItemResponses);
    }
    
    public record FindItemResponse(Long itemId, String name, int price, int discount, double rate) {
        public static FindItemResponse from(final Item item) {
            return new FindItemResponse(item.getId(), item.getName(), item.getPrice(), item.getDiscount(), item.getRate());
        }
    }
}
