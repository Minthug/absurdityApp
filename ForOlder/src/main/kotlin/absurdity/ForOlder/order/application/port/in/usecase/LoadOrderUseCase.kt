package absurdity.ForOlder.order.application.port.`in`.usecase

import absurdity.ForOlder.common.annotation.UseCase
import absurdity.ForOlder.order.domain.Order

@UseCase
interface LoadOrderUseCase {

    fun loadOrderById(orderId: Long): Order
}