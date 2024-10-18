package absurdity.ForOlder.order.adapter.`in`.web

import absurdity.ForOlder.common.dto.BaseResponse
import absurdity.ForOlder.order.application.port.`in`.usecase.ModifyOrderStatusUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ModifyOrderStatusController (
    private val modifyOrderStatusUseCase: ModifyOrderStatusUseCase
) {
    @PatchMapping("/api/order/{orderId}")
    fun modifyOrderStatus(@PathVariable("orderId") orderId: Long) =
        ResponseEntity.ok(
            BaseResponse(modifyOrderStatusUseCase.modifyOrderStatus(orderId))
        )
}