package forOlderJava.absurdityAppForJava.domain.item.service.response;

import java.util.List;
import java.util.stream.Collectors;

public record FindNewItemsResponse(List<FindNewItemResponse> items) {

    public static FindNewItemsResponse from(List<FindNewItemResponse> items) {
        List<FindNewItemResponse> findNewItemResponses = items.stream()
                .map(item -> FindNewItemResponse.of(item.itemId, item.name, item.price, item.discount, item.orderCount ,item.rate))
                .collect(Collectors.toList());
        return new FindNewItemsResponse(findNewItemResponses);
    }


    public record FindNewItemResponse(Long itemId, String name, int price, int discount, Long orderCount, double rate) {

        public static FindNewItemResponse of(final Long itemId, final String name, final int price, final int discount, final Long orderCount,
                                             final double rate) {
            return new FindNewItemResponse(itemId, name, price, discount, orderCount ,rate);
        }
    }
}
