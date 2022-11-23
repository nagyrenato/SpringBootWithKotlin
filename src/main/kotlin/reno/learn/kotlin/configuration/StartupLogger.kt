package reno.learn.kotlin.configuration

import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.EnumerablePropertySource

@Configuration
class StartupLogger {

    private val logger = KotlinLogging.logger {}

    @EventListener
    fun handleContextRefreshed(event: ContextRefreshedEvent) {
        printActiveProperties(event.applicationContext.environment as ConfigurableEnvironment)
    }

    fun printActiveProperties(env: ConfigurableEnvironment) {
        logger.info { "************************* ACTIVE APP PROPERTIES ******************************" }
        env.propertySources
            .asSequence()
            .filter { it.name.contains("application") }
            .map { it as EnumerablePropertySource<*> }
            .map { it.propertyNames.toList() }
            .flatten()
            .distinctBy { it }
            .sortedBy { it }
            .toList()
            .forEach { it ->
                try {
                    logger.info("$it=${env.getProperty(it)}")
                } catch (e: Exception) {
                    logger.warn("$it -> ${e.message}")
                }
            }
        logger.info { "******************************************************************************" }
    }
}
