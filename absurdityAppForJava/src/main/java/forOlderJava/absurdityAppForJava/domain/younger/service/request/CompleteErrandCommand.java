package forOlderJava.absurdityAppForJava.domain.younger.service.request;

public record CompleteErrandCommand(Long errandId, Long youngerId) {

    public static CompleteErrandCommand of(final Long errandId, final Long youngerId) {
        return new CompleteErrandCommand(errandId, youngerId);
    }
}
