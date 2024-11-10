package forOlderJava.absurdityAppForJava.domain.younger.service.request;

public record FindErrandDetailCommand(Long errandId) {

    public static FindErrandDetailCommand from(final Long errandId) {
        return new FindErrandDetailCommand(errandId);
    }
}
