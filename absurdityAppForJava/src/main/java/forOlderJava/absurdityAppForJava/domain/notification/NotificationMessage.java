package forOlderJava.absurdityAppForJava.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

@Getter
@RequiredArgsConstructor
public enum NotificationMessage {

    REGISTER_DELIVERY("주문이 접수되었습니다.", "주문하신 {0} 이(가) {1}분내로 도착할 예정 입니다."),
    START_DELIVERY("동생이 움직입니다","동생이 약 {0}분 후에 도착할 예정 입니다."),
    COMPLETE_DELIVERY("배달이 완료되었습니다", "동생을 칭찬 해주세요");

    private final String title;
    private final String contentFormat;

    public String getContentFromFormat(Object... arguments) {
        return MessageFormat.format(contentFormat, arguments);
    }
}
