package reno.learn.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinExampleApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<KotlinExampleApplication>(*args)
}
