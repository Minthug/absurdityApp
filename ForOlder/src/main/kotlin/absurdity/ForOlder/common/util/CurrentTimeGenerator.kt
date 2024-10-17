package absurdity.ForOlder.common.util

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

abstract class CurrentTimeGenerator {

    companion object {
        fun generateCurrentTime() = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}