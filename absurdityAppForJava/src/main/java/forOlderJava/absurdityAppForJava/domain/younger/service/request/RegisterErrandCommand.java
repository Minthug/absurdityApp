package forOlderJava.absurdityAppForJava.domain.younger.service.request;

public record RegisterErrandCommand(Long orderId, Long memberId, int estimateMinutes) {

    public static RegisterErrandCommand of(final Long orderId, final Long memberId, final int estimateMinutes) {
        return new RegisterErrandCommand(orderId, memberId, estimateMinutes);
    }
}
