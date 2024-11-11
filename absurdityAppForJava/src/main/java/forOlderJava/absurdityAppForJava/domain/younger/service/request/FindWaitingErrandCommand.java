package forOlderJava.absurdityAppForJava.domain.younger.service.request;

import org.springframework.data.domain.Pageable;

public record FindWaitingErrandCommand(Pageable pageable) {
    public static FindWaitingErrandCommand from(final Pageable pageable) {
        return new FindWaitingErrandCommand(pageable);
    }
}
