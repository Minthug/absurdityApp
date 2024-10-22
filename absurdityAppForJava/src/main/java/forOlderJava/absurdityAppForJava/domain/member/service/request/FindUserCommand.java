package forOlderJava.absurdityAppForJava.domain.member.service.request;

public record FindUserCommand(Long memberId) {

    public static FindUserCommand from(Long memberId) {
        return new FindUserCommand(memberId);
    }
}
