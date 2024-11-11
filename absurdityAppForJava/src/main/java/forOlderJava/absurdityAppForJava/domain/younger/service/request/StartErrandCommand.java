package forOlderJava.absurdityAppForJava.domain.younger.service.request;

public record StartErrandCommand(Long errandId, int errandEstimateMinutes, Long youngerId) {

    public static StartErrandCommand of(final Long errandId, final Integer errandEstimateMinutes, final Long youngerId) {
        return new StartErrandCommand(errandId, errandEstimateMinutes, youngerId);
    }
}
