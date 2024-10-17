package absurdity.ForOlder.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.sql.Timestamp

class ErrorResponse (
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val serial: String? = null,
    val message: String,
    val timestamp: String
)