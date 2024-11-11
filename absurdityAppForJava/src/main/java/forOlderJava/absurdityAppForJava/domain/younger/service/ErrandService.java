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
import forOlderJava.absurdityAppForJava.domain.younger.Younger;
import forOlderJava.absurdityAppForJava.domain.younger.exception.AlreadyRegisteredErrandException;
import forOlderJava.absurdityAppForJava.domain.younger.exception.NotFoundErrandException;
import forOlderJava.absurdityAppForJava.domain.younger.exception.NotFoundYoungerException;
import forOlderJava.absurdityAppForJava.domain.younger.repository.ErrandRepository;
import forOlderJava.absurdityAppForJava.domain.younger.repository.YoungerRepository;
import forOlderJava.absurdityAppForJava.domain.younger.service.request.*;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindErrandByOrderResponse;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindErrandDetailResponse;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindWaitingErrandsResponse;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindYoungerErrandsResponse;
import forOlderJava.absurdityAppForJava.global.auth.exception.UnAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static forOlderJava.absurdityAppForJava.domain.notification.NotificationMessage.*;

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
        Member member = findMemberByMemberId(findErrandByOrderCommand.memberId());
        Errand errand = findErrandByOrderWithOrder(findErrandByOrderCommand.orderId());
        checkAuthority(errand, member);

        return FindErrandByOrderResponse.from(errand);
    }

    private Errand findErrandByOrderWithOrder(final Long orderId) {
        return errandRepository.findByOrderIdWithOrder(orderId)
                .orElseThrow(() -> new NotFoundErrandException("존재하지 않는 심부릅 입니다"));
    }

    private void checkAuthority(final Errand errand, final Member member) {
        if (!errand.isOwnByMember(member)) {
            throw new UnAuthenticationException("권한이 없습니다");
        }
    }

    @Transactional(readOnly = true)
    public FindErrandDetailResponse findErrand(FindErrandDetailCommand findErrandDetailCommand) {
        Errand errand = findErrandByErrandIdWithOrderAndOrderItems(findErrandDetailCommand);
        return FindErrandDetailResponse.from(errand);
    }

    private Errand findErrandByErrandIdWithOrderAndOrderItems(FindErrandDetailCommand findErrandDetailCommand) {
        return errandRepository.findByIdWithOrderAndItems(findErrandDetailCommand.errandId())
                .orElseThrow(() -> new NotFoundErrandException("존재하지 않는 심부름 입니다"));
    }

    @Transactional
    public void acceptErrand(AcceptErrandCommand acceptErrandCommand) {
        Younger younger = findYoungerByYoungerId(acceptErrandCommand.youngerId());
        Errand errand = findErrandByErrandIdOptimistic(acceptErrandCommand);
        errand.assignYounger(younger);
    }

    private Errand findErrandByErrandIdOptimistic(AcceptErrandCommand acceptErrandCommand) {
        return errandRepository.findByIdOptimistic(acceptErrandCommand.errandId())
                .orElseThrow(() -> new NotFoundErrandException("존재하지 않는 심부름 입니다"));
    }

    private Younger findYoungerByYoungerId(final Long youngerId) {
        return youngerRepository.findById(youngerId)
                .orElseThrow(() -> new NotFoundYoungerException("존재하지 않는 동생입니다"));
    }

    @Transactional
    public void startErrand(StartErrandCommand startErrandCommand) {
        Younger younger = findYoungerByYoungerId(startErrandCommand.youngerId());
        Errand errand = findErrandByErrandId(startErrandCommand.errandId());
        errand.checkAuthority(younger);
        errand.startErrand(startErrandCommand.errandEstimateMinutes());

        sendStartErrandNotification(startErrandCommand, errand);
    }

    private Errand findErrandByErrandId(final Long errandId) {
        return errandRepository.findById(errandId)
                .orElseThrow(() -> new NotFoundErrandException("존재하지 않는 심부릅 입니다"));
    }

    @Transactional(readOnly = true)
    public FindWaitingErrandsResponse findWaitingErrand(FindWaitingErrandCommand findWaitingErrandCommand) {
        Page<Errand> errandPage = errandRepository.findWaitingErrands(findWaitingErrandCommand.pageable());
        return FindWaitingErrandsResponse.from(errandPage);
    }

    @Transactional
    public void completeErrand(CompleteErrandCommand completeErrandCommand) {
        Younger younger = findYoungerByYoungerId(completeErrandCommand.youngerId());
        Errand errand = findErrandByErrandId(completeErrandCommand.errandId());
        errand.checkAuthority(younger);
        errand.completeErrand();
        sendCompleteErrandNotification(errand);
    }

    private void sendCompleteErrandNotification(Errand errand) {
        SendNotificationCommand command = SendNotificationCommand.of(
                errand.getMemberId(),
                COMPLETE_ERRAND.getTitle(),
                COMPLETE_ERRAND.getContentFromFormat(),
                NotificationType.DELIVERY);

        notificationService.sendNotification(command);
    }

    @Transactional
    public FindYoungerErrandsResponse findYoungerErrands(FindYoungerErrandCommand findYoungerErrandCommand) {

    }

    private void sendStartErrandNotification(StartErrandCommand startErrandCommand, Errand errand) {
        SendNotificationCommand sendNotificationCommand = SendNotificationCommand.of(
                errand.getMemberId(),
                START_ERRAND.getTitle(),
                START_ERRAND.getContentFromFormat(startErrandCommand.errandEstimateMinutes()),
                NotificationType.DELIVERY);

        notificationService.sendNotification(sendNotificationCommand);
    }

}
