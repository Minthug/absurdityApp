package absurdity.ForOlder.order.adapter.out.kafka.produce.dto

import absurdity.ForOlder.order.adapter.out.kafka.event.dto.ModifyOrderStatusEventDto

data class ModifyOrderStatusKafkaDto(
    val modifyOrderStatusEventDto: ModifyOrderStatusEventDto,
    val orderEvent: OrderEvent
)
