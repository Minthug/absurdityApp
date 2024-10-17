package absurdity.ForOlder.order.domain

data class OrderInfo (
    val orderId: Long,
    val brotherId: Long,
    val errandPrice: Int,
    val delStatus: Boolean,
    var orderStatus: OrderStatus
)