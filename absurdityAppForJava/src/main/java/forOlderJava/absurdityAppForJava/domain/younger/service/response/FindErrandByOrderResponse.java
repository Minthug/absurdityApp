package forOlderJava.absurdityAppForJava.domain.younger.service.response;

import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus;

import java.time.LocalDateTime;

public record FindErrandByOrderResponse(Long errandId, ErrandStatus errandStatus, LocalDateTime createdAt, LocalDateTime arrivedAt,
                                        Long orderId, String orderName, int orderPrice, String youngerRequest) {

    public static FindErrandByOrderResponse from(final Errand errand) {
        return new FindErrandByOrderResponse(
                errand.getId(),
                errand.getErrandStatus(),
                errand.getCreatedAt(),
                errand.getArrivedAt(),
                errand.getOrder().getId(),
                errand.getOrder().getName(),
                errand.getOrder().getOrderInfo().getPrice(),
                errand.getOrder().getYoungerRequest()
        );
    }
}
