package absurdity.ForOlder.common

import absurdity.ForOlder.common.exception.ErrorCode

class CustomException (
    val errorCode: ErrorCode
) : RuntimeException()