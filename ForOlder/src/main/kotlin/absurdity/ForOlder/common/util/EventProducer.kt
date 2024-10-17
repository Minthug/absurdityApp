package absurdity.ForOlder.common.util

import org.springframework.context.ApplicationEventPublisher

@Util
class EventProducer private constructor(
    applicationEventPublisher: ApplicationEventPublisher
) {
    init {
        eventPublisher = applicationEventPublisher
    }

    companion object {
        private lateinit var eventPublisher: ApplicationEventPublisher

        fun produceEvent(event: Any) {
            eventPublisher.publishEvent(event)
        }
    }
}