package forOlderJava.absurdityAppForJava.domain.member.controller;

import forOlderJava.absurdityAppForJava.domain.member.exception.MemberException;
import forOlderJava.absurdityAppForJava.domain.member.service.MemberService;
import forOlderJava.absurdityAppForJava.domain.member.service.request.FindUserCommand;
import forOlderJava.absurdityAppForJava.domain.member.service.response.FindUserDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import forOlderJava.absurdityAppForJava.global.auth.oauth.client.OAuthRestClient;
import forOlderJava.absurdityAppForJava.global.util.ErrorTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.processing.Find;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final OAuthRestClient oAuthRestClient;

    @GetMapping("/me")
    public ResponseEntity<FindUserDetailResponse> findMember(@LoginUser Long memberId) {
        FindUserDetailResponse findUserDetailResponse =
                memberService.findMember(FindUserCommand.from(memberId));
        return ResponseEntity.ok(findUserDetailResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@LoginUser Long memberId) {
        FindUserCommand findUserDetailCommand = FindUserCommand.from(memberId);
        FindUserDetailResponse findUserDetailResponse = memberService.findMember(findUserDetailCommand);
        oAuthRestClient.callUnlinkOAuthUser(findUserDetailResponse);
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorTemplate> memberExHandler(MemberException ex) {
        return ResponseEntity.badRequest()
                .body(ErrorTemplate.of(ex.getMessage()));
    }
}
