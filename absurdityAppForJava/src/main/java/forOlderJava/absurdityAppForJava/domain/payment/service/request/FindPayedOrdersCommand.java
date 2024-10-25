package forOlderJava.absurdityAppForJava.domain.payment.service.request;

public record FindPayedOrdersCommand(Long memberId, int page) {
    public static FindPayedOrdersCommand of(final Long memberId, final int page) {
        return new FindPayedOrdersCommand(memberId, page);
    }
}
