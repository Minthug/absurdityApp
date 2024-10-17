package absurdity.ForOlder.order.adapter.out.persistence.repository

import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderItemJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository: JpaRepository<OrderItemJpaEntity, Long> {

    fun findByOrderId(orderId: Long) : List<OrderItemJpaEntity>
}