package absurdity.ForOlder.order.adapter.out.persistence.adapter

import absurdity.ForOlder.common.annotation.PersistenceAdapter
import absurdity.ForOlder.order.application.port.out.CreateOrderPort
import org.aspectj.apache.bcel.Repository

@PersistenceAdapter
class CreateOrderPersistenceAdapter (

    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
): CreateOrderPort {
    
}