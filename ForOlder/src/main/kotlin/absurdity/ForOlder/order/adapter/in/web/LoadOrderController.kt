package absurdity.ForOlder.order.adapter.`in`.web

import absurdity.ForOlder.common.dto.BaseResponse
import absurdity.ForOlder.order.application.port.`in`.usecase.LoadOrderUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class LoadOrderController (
    private val loadOrderUseCase : LoadOrderUseCase
) {
    @GetMapping("/api/orders/{orderId}")
    fun loadOrderById(@PathVariable("orderId") orderId: Long) =
        ResponseEntity.ok(
            BaseResponse(loadOrderUseCase.loadOrderById(orderId))
        )
}

