package absurdity.ForOlder.kafka.dto

import org.hibernate.criterion.Order
import java.time.LocalDateTime

data class ModifyOrderStatusKafkaDto(
    val modifyOrderStatusEventDto: ModifyOrderStatusEventDto,
    val orderEvent: OrderEvent
)
