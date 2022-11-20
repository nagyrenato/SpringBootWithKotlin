package reno.learn.kotlin.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["reno.learn.kotlin.repository"])
class MongoDB
