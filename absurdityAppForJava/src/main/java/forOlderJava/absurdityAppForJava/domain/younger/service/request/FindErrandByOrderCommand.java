package forOlderJava.absurdityAppForJava.domain.younger.service.request;

public record FindErrandByOrderCommand(Long memberId, Long orderId) {

    public static FindErrandByOrderCommand of(final Long memberId, final Long orderId) {
        return new FindErrandByOrderCommand(memberId, orderId);
    }
}
