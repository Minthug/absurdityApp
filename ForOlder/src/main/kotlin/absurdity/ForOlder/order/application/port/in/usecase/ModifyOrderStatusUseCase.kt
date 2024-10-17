package absurdity.ForOlder.order.application.port.`in`.usecase

import absurdity.ForOlder.common.annotation.UseCase

@UseCase
fun interface ModifyOrderStatusUseCase {

    fun modifyOrderStatus(orderId: Long): Long
}