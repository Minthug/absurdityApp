package absurdity.ForOlder.order.application.port.`in`.model

data class CreateOrderItemCommand (
    val itemId: Long,
    val itemName: String,
    val itemPrice: Int,
    val itemQuantity: Int,
    val itemTotalPrice: Int
)