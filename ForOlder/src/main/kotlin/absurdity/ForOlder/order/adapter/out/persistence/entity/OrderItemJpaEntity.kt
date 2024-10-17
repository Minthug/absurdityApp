package absurdity.ForOlder.order.adapter.out.persistence.entity

import absurdity.ForOlder.order.domain.OrderStatus
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "ORDER_ENTITY")
data class OrderItemJpaEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ID")
    val orderItemId: Long? = null,

    @Column(name = "ORDER_ID")
    val orderId: Long,

    @Column(name = "ERRAND_ID")
    val itemId: Long,

    @Column(name = "ERRAND_PRICE")
    val itemPrice: Int,

    @Column(name = "ERRAND_NAME")
    val itemName: String,

    @Column(name = "ERRAND_QUANTITY")
    val itemQuantity: Int,

    @Column(name = "ERRAND_PRICE")
    val itemTotalPrice: Int,

    @Column(name = "DEL_STATUS")
    val delStatus: Boolean = false
): DataBaseEntity()