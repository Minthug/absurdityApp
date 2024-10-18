package absurdity.ForOlder.order.adapter.out.persistence.adapter

import absurdity.ForOlder.common.annotation.PersistenceAdapter
import absurdity.ForOlder.common.exception.CustomException
import absurdity.ForOlder.common.exception.ErrorCode
import absurdity.ForOlder.order.adapter.out.persistence.mapper.OrderMapper
import absurdity.ForOlder.order.adapter.out.persistence.repository.OrderItemRepository
import absurdity.ForOlder.order.adapter.out.persistence.repository.OrderRepository
import absurdity.ForOlder.order.application.port.out.LoadOrderPort
import absurdity.ForOlder.order.domain.Order

@PersistenceAdapter
class LoadOrderPersistenceAdapter (
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val orderMapper: OrderMapper,
): LoadOrderPort {
    override fun loadOrderById(orderId: Long): Order {
        val orderJpaEntity = orderRepository.findById(orderId)
            .orElseThrow { throw CustomException(ErrorCode.ORDER_NOT_FOUND)}

        val orderItemList = orderItemRepository.findByOrderId(orderId)

        return orderMapper.mapToDomainEntity(orderJpaEntity, orderItemList)
    }
}