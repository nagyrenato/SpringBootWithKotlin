package reno.learn.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class KotlinExampleApplicationTests {

    @Test
    fun contextLoads(context: ApplicationContext?) {
        assertThat(context).isNotNull
    }
}
