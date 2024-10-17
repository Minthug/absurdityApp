package absurdity.ForOlder.domain

import absurdity.ForOlder.common.exception.CustomException
import absurdity.ForOlder.common.exception.ErrorCode.CANNOT_CANCEL_ORDER
import absurdity.ForOlder.kafka.dto.OrderItem

data class Order (
    val orderInfo: OrderInfo,
    val orderer: Orderer,
    val orderItem: List<OrderItem>,
) {
    fun requestCancel() {
        // 반항하고싶거나 피치 못할 사정이면 취소가 가능하다.
        if (orderInfo.orderStatus == OrderStatus.SHIPPING || orderInfo.orderStatus == OrderStatus.COOKING || orderInfo.orderStatus == OrderStatus.COME_OUT) {
            throw CustomException(CANNOT_CANCEL_ORDER)
        }
        orderInfo.orderStatus = OrderStatus.OLDER_CANCEL_REQUEST
    }
}
