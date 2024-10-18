package absurdity.ForOlder.order.adapter.out.kafka.event.listener

import absurdity.ForOlder.order.adapter.out.kafka.event.dto.SaveOrderEventDto
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.Order
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.OrderEvent
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.OrderItem
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.SaveOrderKafkaDto
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SaveOrderEventListener (
    private val saveOrderKafkaTemplate: KafkaTemplate<String, SaveOrderKafkaDto>
) {

    @Async
    @EventListener(SaveOrderEventDto::class)
    fun handleSaveEvent(orderEventDto: SaveOrderEventDto) {
        saveOrderKafkaTemplate.send("save-order-data", createSaveOrderKafkaDto(orderEventDto))
    }

    private fun createSaveOrderKafkaDto(orderEventDto: SaveOrderEventDto) : SaveOrderKafkaDto {
        val order = Order(
            orderId = orderEventDto.orderJpaEntity.orderId!!,
            olderId = orderEventDto.orderJpaEntity.orderId,
            location = orderEventDto.orderJpaEntity.location,
            brotherId = orderEventDto.orderJpaEntity.brotherId,
            errandPrice = orderEventDto.orderJpaEntity.errandPrice,
            orderStatus = orderEventDto.orderJpaEntity.orderStatus,
            delStatus = orderEventDto.orderJpaEntity.delStatus,
            regDate = orderEventDto.orderJpaEntity.regDate!!,
            modDate = orderEventDto.orderJpaEntity.modDate!!
        )

        val orderItemList = orderEventDto.orderItemJpaEntityList
            .map {
                OrderItem(
                    orderItemId = it.orderItemId!!,
                    orderId = it.orderId,
                    itemId = it.itemId,
                    itemPrice = it.itemPrice,
                    itemName = it.itemName,
                    itemQuantity = it.itemQuantity,
                    itemTotalPrice = it.itemTotalPrice,
                    delStatus = it.delStatus,
                    regDate = it.regDate!!,
                    modDate = it.modDate!!
                )
            }.toList()

        val orderEvent = OrderEvent(
            eventUUID = UUID.randomUUID().toString(),
            orderId = order.orderId
        )

        return SaveOrderKafkaDto(order, orderItemList, orderEvent)
    }
}