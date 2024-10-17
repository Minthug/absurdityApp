package absurdity.ForOlder.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode (
    val status: HttpStatus,
    val serial: String,
    val message: String
) {
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "ERROR_01", "해당 심부름이 없습니다, 확인 해보세요"),
    CANNOT_CANCEL_ORDER(HttpStatus.BAD_REQUEST, "ERROR_02", "안쓸듯")
}