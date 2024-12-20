package absurdity.ForOlder.common.config

import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.ModifyOrderStatusKafkaDto
import absurdity.ForOlder.order.adapter.out.kafka.produce.dto.SaveOrderKafkaDto
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@EnableKafka
@Configuration
class KafkaProducerConfig ( private val environment: Environment ) {

    private val logger = LoggerFactory.getLogger(KafkaProducerConfig::class.java)

    private val bootstrapServers: String
        get() = environment.getProperty("spring.kafka.producer.bootstrap-servers")
            ?: throw IllegalStateException("Kafka bootstrap servers are not configured")

    @Bean(name = ["modifyOrderStatusDataProducerFactory"])
    fun modifyOrderStatusDataProducerFactory() : DefaultKafkaProducerFactory<String, ModifyOrderStatusKafkaDto> {
        return DefaultKafkaProducerFactory(modifyOrderStatusDataProducerConfig())
    }

    @Bean(name = ["modifyOrderStatusDataProducerConfig"])
    fun modifyOrderStatusDataProducerConfig() = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to environment["spring.kafka.producer.bootstrap-servers"],
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to org.springframework.kafka.support.serializer.JsonSerializer::class.java
    )

    @Bean(name = ["modifyOrderStatusKafkaTemplate"])
    fun modifyOrderStatusKafkaTemplate(): KafkaTemplate<String, ModifyOrderStatusKafkaDto> {
        return KafkaTemplate(modifyOrderStatusDataProducerFactory())
    }

    @Bean(name = ["saveOrderServiceDataProducerFactory"])
    fun saveOrderServiceDataProducerFactory(): DefaultKafkaProducerFactory<String, SaveOrderKafkaDto> {
        val config = saveOrderServiceProducerConfig()
        logger.info("Kafka producer config: $config")
        return DefaultKafkaProducerFactory(config)
    }

    @Bean(name = ["saveOrderServiceDataProducerConfig"])
    fun saveOrderServiceProducerConfig() = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to environment["spring.kafka.producer.bootstrap-servers"],
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
    )

    @Bean(name = ["saveOrderKafkaTemplate"])
    fun saveOrderKafkaTemplate(): KafkaTemplate<String, SaveOrderKafkaDto> {
        return KafkaTemplate(saveOrderServiceDataProducerFactory())
    }
}