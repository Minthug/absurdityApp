package absurdity.ForOlder.order.application.port.out

import absurdity.ForOlder.order.domain.Order

fun interface LoadOrderPort {
    fun loadOrderById(orderId: Long): Order
}