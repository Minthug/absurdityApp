package absurdity.ForOlder.order.application.service

import absurdity.ForOlder.common.annotation.UseCase
import absurdity.ForOlder.order.application.port.`in`.model.CreateOrderCommand
import absurdity.ForOlder.order.application.port.`in`.usecase.CreateOrderUseCase
import absurdity.ForOlder.order.application.port.out.CreateOrderPort
import org.springframework.transaction.annotation.Transactional

@UseCase
@Transactional
class CreateOrderService (
    private val createOrderPort: CreateOrderPort
) : CreateOrderUseCase {
    override fun createOrder(orderCommand: CreateOrderCommand): Long {
            // 결제 연동 필요

        return createOrderPort.createOrder(orderCommand)
    }
}