package absurdity.ForOlder.order.application.port.`in`.model

import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.OrderItem
import org.springframework.beans.factory.parsing.Location

data class CreateOrderCommand (
    val brotherId: Long,
    val errandPrice: Int,
    val location: String,
    val orderItems: List<CreateOrderItemCommand>
)