package absurdity.ForOlder.order.adapter.out.persistence.mapper

import absurdity.ForOlder.common.annotation.Mapper
import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderItemJpaEntity
import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderJpaEntity
import absurdity.ForOlder.order.domain.Order
import absurdity.ForOlder.order.domain.OrderInfo
import absurdity.ForOlder.order.domain.OrderItem
import absurdity.ForOlder.order.domain.Orderer

@Mapper
class OrderMapper {

    fun mapToEntity(order: Order) : OrderJpaEntity {
        return OrderJpaEntity(
            orderId = order.orderInfo.orderId,
            olderId = order.orderer.olderId,
            location = order.orderer.location,
            brotherId = order.orderInfo.brotherId,
            errandPrice = order.orderInfo.errandPrice,
            orderStatus = order.orderInfo.orderStatus,
            delStatus = order.orderInfo.delStatus
        )
    }

    fun mapToDomainEntity(orderJpaEntity: OrderJpaEntity, orderItemListEntity: List<OrderItemJpaEntity>): Order {
        val orderInfo = OrderInfo (
            orderId = orderJpaEntity.orderId!!,
            brotherId = orderJpaEntity.brotherId,
            errandPrice = orderJpaEntity.errandPrice,
            delStatus = orderJpaEntity.delStatus,
            orderStatus = orderJpaEntity.orderStatus
        )

        val orderItemList = orderItemListEntity.map {
            OrderItem(
                itemName = it.itemName,
                itemPrice = it.itemPrice,
                itemQuantity = it.itemQuantity
            )
        }.toList()

        val orderer = Orderer(
            olderId = orderJpaEntity.olderId,
            name = "Dev Minthug",
            location = orderJpaEntity.location,
            phoneNumber = "010-1111-1111"
        )
        return Order(orderInfo, orderer, orderItemList)
    }
}