package forOlderJava.absurdityAppForJava.domain.younger.service.response;

import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record FindWaitingErrandsResponse(List<FindWaitingErrandResponse> errandResponses, int page, long totalElements) {


    public static FindWaitingErrandsResponse from(final Page<Errand> errands) {
        Page<FindWaitingErrandResponse> errandResponsePage = errands.map(FindWaitingErrandResponse::from);

        return new FindWaitingErrandsResponse(
                errandResponsePage.getContent(),
                errandResponsePage.getNumber(),
                errands.getTotalElements()
        );
    }

    public record FindWaitingErrandResponse(Long errandId, LocalDateTime createdAt, LocalDateTime arrivedAt,
                                            String location, int tip) {

        public static FindWaitingErrandResponse from(final Errand errand) {
            return new FindWaitingErrandResponse(
                    errand.getId(),
                    errand.getCreatedAt(),
                    errand.getArrivedAt(),
                    errand.getLocation(),
                    errand.getTip()
            );
        }
    }
}
