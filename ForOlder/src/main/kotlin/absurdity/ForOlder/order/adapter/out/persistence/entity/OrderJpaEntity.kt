package absurdity.ForOlder.order.adapter.out.persistence.entity

import absurdity.ForOlder.order.domain.OrderStatus
import javax.persistence.*

@Entity(name = "ORDER")
data class OrderJpaEntity (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    val orderId: Long? = null,

    @Column(name = "OLDER_ID")
    val olderId: Long,

    @Column(name = "LOCATION")
    val location: String,

    @Column(name = "BROTHER_ID")
    val brotherId: Long,

    @Column(name = "ERRAND_PRICE")
    val errandPrice: Int,

    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    var orderStatus: OrderStatus,

    @Column(name = "DEL_STATUS")
    var delStatus: Boolean = false
): DataBaseEntity() {
    fun updateOrderStatus(orderStatus: OrderStatus) {
        this.orderStatus = orderStatus
    }
}