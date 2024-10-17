package absurdity.ForOlder.order.application.port.`in`.usecase

import absurdity.ForOlder.common.annotation.UseCase
import absurdity.ForOlder.order.application.port.`in`.model.CreateOrderCommand

@UseCase
interface CreateOrderUseCase {

    fun createOrder(orderCommand: CreateOrderCommand): Long

}