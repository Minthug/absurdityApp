package forOlderJava.absurdityAppForJava.domain.younger.service.request;

public record AcceptErrandCommand(Long errandId ,Long youngerId) {
    public static AcceptErrandCommand of(Long errandId, Long youngerId) {
        return new AcceptErrandCommand(errandId, youngerId);
    }
}
