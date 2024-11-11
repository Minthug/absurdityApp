package forOlderJava.absurdityAppForJava.domain.younger.service.request;

import forOlderJava.absurdityAppForJava.domain.younger.ErrandStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record FindYoungerErrandCommand(Long youngerId, List<ErrandStatus> errandStatuses, Pageable pageable) {

    public static FindYoungerErrandCommand of(final Long youngerId, final List<ErrandStatus> errandStatuses, final Pageable pageable) {
        return new FindYoungerErrandCommand(youngerId, errandStatuses, pageable);
    }
}
