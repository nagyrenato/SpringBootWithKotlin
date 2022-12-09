package reno.learn.kotlin.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Profile(value = ["dev", "prod", "int"])
@Configuration
@EnableMongoRepositories(basePackages = ["reno.learn.kotlin.repository"])
class MongoDB
