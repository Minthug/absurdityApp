package forOlderJava.absurdityAppForJava.domain.younger.service.response;

import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus;

import java.time.LocalDateTime;
import java.util.List;

public record FindYoungerErrandsResponse(List<FindYoungerErrandResponse> errands, int page, long totalElements) {

    public static FindYoungerErrandsResponse of(final List<Errand> contents,
                                                final int page,
                                                final long totalElements) {
        List<FindYoungerErrandResponse> errandResponses = contents.stream()
                .map(FindYoungerErrandResponse::from)
                .toList();
        return new FindYoungerErrandsResponse(errandResponses, page, totalElements);
    }


    public record FindYoungerErrandResponse(Long errandId, ErrandStatus errandStatus, LocalDateTime createdAt,
                                            LocalDateTime arrivedAt, String location, int orderPrice,
                                            String youngerRequest, int tip) {

        public static FindYoungerErrandResponse from(final Errand errand) {
            return new FindYoungerErrandResponse(
                    errand.getId(),
                    errand.getErrandStatus(),
                    errand.getCreatedAt(),
                    errand.getArrivedAt(),
                    errand.getLocation(),
                    errand.getOrder().getOrderInfo().getPrice(),
                    errand.getOrder().getYoungerRequest(),
                    errand.getTip()
            );
        }

    }
}
