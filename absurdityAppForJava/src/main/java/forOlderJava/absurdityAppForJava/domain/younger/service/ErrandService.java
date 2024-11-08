package forOlderJava.absurdityAppForJava.domain.younger.service;

import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.notification.NotificationType;
import forOlderJava.absurdityAppForJava.domain.notification.service.NotificationService;
import forOlderJava.absurdityAppForJava.domain.notification.service.request.SendNotificationCommand;
import forOlderJava.absurdityAppForJava.domain.order.entity.Order;
import forOlderJava.absurdityAppForJava.domain.order.exception.NotFoundOrderException;
import forOlderJava.absurdityAppForJava.domain.order.repository.OrderRepository;
import forOlderJava.absurdityAppForJava.domain.younger.Errand;
import forOlderJava.absurdityAppForJava.domain.younger.exception.AlreadyRegisteredErrandException;
import forOlderJava.absurdityAppForJava.domain.younger.repository.ErrandRepository;
import forOlderJava.absurdityAppForJava.domain.younger.repository.YoungerRepository;
import forOlderJava.absurdityAppForJava.domain.younger.service.request.FindErrandByOrderCommand;
import forOlderJava.absurdityAppForJava.domain.younger.service.request.RegisterErrandCommand;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindErrandByOrderResponse;
import forOlderJava.absurdityAppForJava.global.auth.exception.UnAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static forOlderJava.absurdityAppForJava.domain.notification.NotificationMessage.REGISTER_ERRAND;

@Service
@RequiredArgsConstructor
public class ErrandService {

    private final ErrandRepository errandRepository;
    private final MemberRepository memberRepository;
    private final YoungerRepository youngerRepository;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    @Transactional
    public Long registerErrand(RegisterErrandCommand registerErrandCommand) {
        checkMemberHasRegisterErrandAuthority(registerErrandCommand.memberId());
        Order order = findOrderByOrderIdPessimistic(registerErrandCommand);
        checkAlreadyRegisteredErrand(order);
        Errand errand = new Errand(order, registerErrandCommand.estimateMinutes());
        errandRepository.save(errand);

        sendRegisterErrandNotification(registerErrandCommand, errand, order);
        return errand.getId();
    }

    private Order findOrderByOrderIdPessimistic(RegisterErrandCommand registerErrandCommand) {
        return orderRepository.findByIdPessimistic(registerErrandCommand.orderId())
                .orElseThrow(() -> new NotFoundOrderException("존재하지 않는 주문 입니다"));
    }

    private void checkAlreadyRegisteredErrand(final Order order) {
        if (errandRepository.existsByOrder(order)) {
            throw new AlreadyRegisteredErrandException("이미 심부름이 생성된 주문 입니다");
        }
    }

    private void checkMemberHasRegisterErrandAuthority(final Long memberId) {
        Member member = findMemberByMemberId(memberId);
        if (!member.isYounger()) {
            throw new UnAuthenticationException("권한이 없습니다");
        }
    }

    private Member findMemberByMemberId(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("존재하지 않는 유저 입니다"));
    }

    private void sendRegisterErrandNotification(RegisterErrandCommand registerErrandCommand, Errand errand, Order order) {
        SendNotificationCommand notificationCommand = SendNotificationCommand.of(errand.getMemberId(),
                REGISTER_ERRAND.getTitle(),
                REGISTER_ERRAND.getContentFromFormat(order.getName(), registerErrandCommand.estimateMinutes()),
                NotificationType.DELIVERY);
        notificationService.sendNotification(notificationCommand);
    }


    @Transactional
    public FindErrandByOrderResponse findErrandByOrder(FindErrandByOrderCommand findErrandByOrderCommand) {

    }
}
