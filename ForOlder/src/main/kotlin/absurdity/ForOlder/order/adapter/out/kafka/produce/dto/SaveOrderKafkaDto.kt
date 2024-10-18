package absurdity.ForOlder.order.adapter.out.kafka.produce.dto

import absurdity.ForOlder.order.domain.OrderStatus
import java.time.LocalDateTime

data class SaveOrderKafkaDto(
    val order: absurdity.ForOlder.order.adapter.out.kafka.produce.dto.Order,
    val orderItem: List<OrderItem>,
    val orderEvent: OrderEvent
)

data class Order (
    val orderId: Long,
    val olderId: Long, // memberId
    val location: String,
    val brotherId: Long,  // storeId
    val errandPrice: Int, // totalPrice
    val orderStatus: OrderStatus,
    val delStatus: Boolean,
    val regDate: LocalDateTime,
    val modDate: LocalDateTime
)

data class OrderItem (
    val orderItemId: Long,
    val orderId: Long,
    val itemId: Long,
    val itemPrice: Int,
    val itemName: String,
    val itemQuantity: Int,
    val itemTotalPrice: Int,
    val delStatus: Boolean,
    val regDate: LocalDateTime,
    val modDate: LocalDateTime
)

data class OrderEvent (
    val eventUUID: String,
    val orderId: Long
)