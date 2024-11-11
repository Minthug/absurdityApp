package forOlderJava.absurdityAppForJava.domain.younger.controller;

import forOlderJava.absurdityAppForJava.domain.younger.exception.AlreadyAssignedErrandException;
import forOlderJava.absurdityAppForJava.domain.younger.service.ErrandService;
import forOlderJava.absurdityAppForJava.domain.younger.service.request.*;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindErrandByOrderResponse;
import forOlderJava.absurdityAppForJava.domain.younger.service.response.FindErrandDetailResponse;
import forOlderJava.absurdityAppForJava.global.auth.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

import java.net.URI;

@RestController
@RequestMapping("/v1/errands")
@RequiredArgsConstructor
public class ErrandController {

    private static final String BASE_URI = "/v1/errands/";
    private final ErrandService errandService;

    @PostMapping("/orders/{orderId}/errands")
    public ResponseEntity<Void> registerErrands(@PathVariable(value = "orderId") final Long orderId,
                                                @RequestBody final RegisterErrandRequest registerErrandRequest,
                                                @LoginUser final Long memberId) {
        RegisterErrandCommand registerErrandCommand = RegisterErrandCommand.of(orderId, memberId, registerErrandRequest.estimateMinutes());
        Long errandId = errandService.registerErrand(registerErrandCommand);
        URI location = URI.create(BASE_URI + errandId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{errandId}")
    public ResponseEntity<FindErrandDetailResponse> findErrand(@PathVariable(value = "errandId") final Long errandId) {
        FindErrandDetailCommand errandDetailCommand = FindErrandDetailCommand.from(errandId);
        FindErrandDetailResponse findErrandDetailResponse = errandService.findErrand(errandDetailCommand);
        return ResponseEntity.ok(findErrandDetailResponse);
    }

    @GetMapping("/order/{orderId}/errands")
    public ResponseEntity<FindErrandByOrderResponse> findErrandByOrder(@PathVariable final Long orderId,
                                                                       @LoginUser final Long memberId) {
        FindErrandByOrderCommand errandByOrderCommand = FindErrandByOrderCommand.of(orderId, memberId);
        FindErrandByOrderResponse findErrandByOrderResponse = errandService.findErrandByOrder(errandByOrderCommand);
        return ResponseEntity.ok(findErrandByOrderResponse);
    }

    @PatchMapping("/{errandId}/accept")
    public ResponseEntity<Void> acceptErrand(@PathVariable final Long errandId,
                                             @LoginUser final Long youngerId) {
        AcceptErrandCommand acceptErrandCommand = AcceptErrandCommand.of(errandId, youngerId);
        acceptErrandIfNotAssigned(acceptErrandCommand);
        return ResponseEntity.noContent().build();
    }

    private void acceptErrandIfNotAssigned(AcceptErrandCommand acceptErrandCommand) {
        try {
            errandService.acceptErrand(acceptErrandCommand);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new AlreadyAssignedErrandException("이미 완료된 심부름 입니다");
        }
    }

    @PatchMapping("/{errandId}/pickup")
    public ResponseEntity<Void> startErrand(@PathVariable final Long errandId,
                                            @RequestBody @Valid StartErrandRequest startErrandRequest,
                                            @LoginUser final Long youngerId) {
        StartErrandCommand startErrandCommand = StartErrandCommand.of(errandId, startErrandRequest.errandEstimateMinutes(), youngerId);
        errandService.startErrand(startErrandCommand);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{errandId}/complete")
    public ResponseEntity<Void> completeErrand(@PathVariable final Long errandId,
                                               @LoginUser final Long youngerId) {
        CompleteErrandCommand completeErrandCommand = CompleteErrandCommand.of(errandId, youngerId);
        errandService.completeErrand(completeErrandCommand);
        return ResponseEntity.noContent().build();
    }
}
