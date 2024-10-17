package absurdity.ForOlder.order.application.port.out

import absurdity.ForOlder.order.application.port.`in`.model.CreateOrderCommand

fun interface CreateOrderPort {
    fun createOrder(orderCommand: CreateOrderCommand): Long
}