package absurdity.ForOlder.order.adapter.out.persistence.adapter

import absurdity.ForOlder.common.annotation.PersistenceAdapter
import absurdity.ForOlder.common.util.EventProducer
import absurdity.ForOlder.order.adapter.out.kafka.event.dto.SaveOrderEventDto
import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderItemJpaEntity
import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderJpaEntity
import absurdity.ForOlder.order.adapter.out.persistence.repository.OrderItemRepository
import absurdity.ForOlder.order.adapter.out.persistence.repository.OrderRepository
import absurdity.ForOlder.order.application.port.`in`.model.CreateOrderCommand
import absurdity.ForOlder.order.application.port.out.CreateOrderPort
import absurdity.ForOlder.order.domain.OrderStatus
import org.aspectj.apache.bcel.Repository

@PersistenceAdapter
class CreateOrderPersistenceAdapter (

    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
): CreateOrderPort {
    override fun createOrder(orderCommand: CreateOrderCommand): Long {
        val order =OrderJpaEntity(
            olderId = 1L,
            brotherId = orderCommand.brotherId,
            location = orderCommand.location,
            orderStatus = OrderStatus.CHECK,
            errandPrice = orderCommand.errandPrice
        )

        orderRepository.save(order)

        val orderItems = orderCommand.orderItems.map {
            OrderItemJpaEntity(
                itemId = it.itemId,
                itemName = it.itemName,
                itemQuantity = it.itemQuantity,
                itemPrice = it.itemPrice,
                itemTotalPrice = it.itemTotalPrice,
                orderId = order.orderId!!,
            )
        }

        orderItemRepository.saveAll(orderItems)

        EventProducer.produceEvent(
            SaveOrderEventDto(
                orderJpaEntity = order,
                orderItemJpaEntityList = orderItems
            )
        )
        return order.orderId!!
    }
}