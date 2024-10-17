package absurdity.ForOlder.order.adapter.out.kafka.event.dto

import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderItemJpaEntity
import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderJpaEntity

data class SaveOrderEventDto (
    val orderJpaEntity: OrderJpaEntity,
    val orderItemJpaEntityList: List<OrderItemJpaEntity>
)