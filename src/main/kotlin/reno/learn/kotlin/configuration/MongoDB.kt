package reno.learn.kotlin.configuration

import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["reno.learn.kotlin.repository"])
class MongoDB {

    private val logger = KotlinLogging.logger {}
    init {
        logger.debug { "Initializing MongoDB" }
    }
}
