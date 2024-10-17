package absurdity.ForOlder.kafka.dto

import absurdity.ForOlder.domain.OrderStatus
import org.hibernate.criterion.Order
import java.time.LocalDateTime

data class SaveOrderKafkaDto(
    val order: Order,
    val orderItem: List<OrderItem>,
    val orderEvent: OrderEvent
)

data class Order (
    val orderId: Long,
    val olderId: Long,
    val brotherId: Long,
    val errandPrice: Int,
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