package absurdity.ForOlder.order.domain

enum class OrderStatus (
    val description: String
) {

    CHECK("심부름 접수"),
    APPROVAL("심부름 승인"),
    OLDER_CANCEL_REQUEST("형/누나의 변덕"),
    OLDER_CANCEL("단순변심"),
    YOUNGER_CANCEL("반항"),
    COOKING("요리 중"),
    SHIPPING("가능 중"),
    COME_OUT("나오세요")

    /*
     RECEIPT("주문 접수"),
    USER_CANCEL_REQUEST("사용자 취소 요청"),
    USER_CANCEL("사용자 취소"),
    STORE_CANCEL("가게 사정 취소"),
    COOKING("조리 중"),
    SHIPPING("배달 중"),
    APPROVAL("주문 승인")
     */
}