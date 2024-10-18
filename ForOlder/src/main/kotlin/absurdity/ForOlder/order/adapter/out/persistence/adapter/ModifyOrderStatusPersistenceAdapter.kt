package absurdity.ForOlder.order.adapter.out.persistence.adapter

import absurdity.ForOlder.common.annotation.PersistenceAdapter
import absurdity.ForOlder.common.util.EventProducer
import absurdity.ForOlder.order.adapter.out.kafka.event.dto.ModifyOrderStatusEventDto
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.ModifyOrderStatusKafkaDto
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.OrderEvent
import absurdity.ForOlder.order.adapter.out.persistence.repository.OrderRepository
import absurdity.ForOlder.order.application.port.out.ModifyOrderStatusPort
import absurdity.ForOlder.order.domain.Order
import java.util.*

@PersistenceAdapter
class ModifyOrderStatusPersistenceAdapter (
    private val orderRepository: OrderRepository
): ModifyOrderStatusPort {

    override fun modifyOrderStatus(order: Order): Long {
        orderRepository.modifyOrderStatus(order.orderInfo.orderStatus, order.orderInfo.orderId)

        EventProducer.produceEvent(
            ModifyOrderStatusKafkaDto(
                modifyOrderStatusEventDto = ModifyOrderStatusEventDto(
                    orderId = order.orderInfo.orderId,
                    orderStatus = order.orderInfo.orderStatus),

                OrderEvent(
                    eventUUID = UUID.randomUUID().toString(),
                    orderId = order.orderInfo.orderId)
            )
        )

        return order.orderInfo.orderId
    }
}