package absurdity.ForOlder.order.adapter.out.persistence.repository

import absurdity.ForOlder.order.adapter.out.persistence.entity.OrderEventJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderEventRepository: JpaRepository<OrderEventJpaEntity, Long>