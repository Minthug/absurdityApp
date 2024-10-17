package absurdity.ForOlder.order.application.port.out

import absurdity.ForOlder.order.domain.Order

fun interface ModifyOrderStatusPort {
    fun modifyOrderStatus(order: Order): Long
}