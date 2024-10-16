package absurdity.ForOlder.domain

import java.time.Instant

data class Call (
    val id: Int? = null,
    val timeStomp: Instant = Instant.now(),
    val fromBro: String,
    val toBro: String,
    val status: CallStatus
)

enum class CallStatus {
    INITIATED, RECEIVED, MISSED
}