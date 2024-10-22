package forOlderJava.absurdityAppForJava.domain.member.service;

import forOlderJava.absurdityAppForJava.domain.cart.repository.CartItemRepository;
import forOlderJava.absurdityAppForJava.domain.cart.repository.CartRepository;
import forOlderJava.absurdityAppForJava.domain.coupon.repository.UserCouponRepository;
import forOlderJava.absurdityAppForJava.domain.member.Member;
import forOlderJava.absurdityAppForJava.domain.member.exception.NotFoundMemberException;
import forOlderJava.absurdityAppForJava.domain.member.repository.MemberRepository;
import forOlderJava.absurdityAppForJava.domain.member.service.request.FindUserCommand;
import forOlderJava.absurdityAppForJava.domain.member.service.request.RegisterUserCommand;
import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.domain.member.service.response.RegisterMemberResponse;
import forOlderJava.absurdityAppForJava.domain.order.repository.OrderRepository;
import forOlderJava.absurdityAppForJava.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    public RegisterMemberResponse getOrRegisterMember(final RegisterUserCommand registryUserCommand) {
        Member findMember = memberRepository.findByProviderAndProviderId(
                        registryUserCommand.provider(),
                        registryUserCommand.providerId())
                .orElseGet(() -> {
                    Member member = Member.builder()
                            .nickname(registryUserCommand.nickname())
                            .email(registryUserCommand.email())
                            .provider(registryUserCommand.provider())
                            .providerId(registryUserCommand.providerId())
                            .memberRole(registryUserCommand.memberRole())
                            .memberGrade(registryUserCommand.memberGrade())
                            .build();
                    memberRepository.save(member);
                    return member;
                });
        return RegisterMemberResponse.from(findMember);
    }

    @Transactional
    public FindUserDetailResponse findMember(FindUserCommand findUserCommand) {
        Member findMember = findMemberByMemberId(findUserCommand.memberId());
        return FindUserDetailResponse.from(findMember);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = findMemberByMemberId(memberId);
//        releaseDeliveryAboutUserInfo(findMember);
        cartItemRepository.deleteByMember(findMember);
        cartRepository.deleteByMember(findMember);
        paymentRepository.deleteByMember(findMember);
        orderRepository.deleteByMember(findMember);
        userCouponRepository.deleteByMember(findMember);
        memberRepository.delete(findMember);
    }

//    private void releaseDeliveryAboutUserInfo(Member findMember) {
//        List<>
//    }

    private Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("존재하지 않은 유저 입니다"));
    }
}
