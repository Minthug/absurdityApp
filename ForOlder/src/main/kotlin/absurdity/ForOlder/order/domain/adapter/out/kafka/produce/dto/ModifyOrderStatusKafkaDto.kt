package absurdity.ForOlder.order.domain.adapter.out.kafka.produce.dto

import absurdity.ForOlder.order.domain.adapter.out.kafka.event.dto.ModifyOrderStatusEventDto

data class ModifyOrderStatusKafkaDto(
    val modifyOrderStatusEventDto: ModifyOrderStatusEventDto,
    val orderEvent: OrderEvent
)
