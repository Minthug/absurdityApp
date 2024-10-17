package absurdity.ForOlder.order.adapter.out.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository

interface OrderEventRepository: JpaRepository<OrderEventRepository, Long> {
}