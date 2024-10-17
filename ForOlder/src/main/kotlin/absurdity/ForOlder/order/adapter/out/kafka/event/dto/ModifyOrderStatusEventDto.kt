package absurdity.ForOlder.order.adapter.out.kafka.event.dto

import absurdity.ForOlder.order.domain.OrderStatus

data class ModifyOrderStatusEventDto (
    val orderId: Long,
    val orderStatus: OrderStatus
)