package forOlderJava.absurdityAppForJava.domain.payment.service.request;

import lombok.Builder;

@Builder
public record FindPayedOrdersCommand(Long memberId, int page) {
    public static FindPayedOrdersCommand of(final Long memberId, final int page) {
        return new FindPayedOrdersCommand(memberId, page);
    }
}
