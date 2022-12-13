package reno.learn.kotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Order
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
class AbbreviationControllerIT {
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val objectMapper = ObjectMapper()

    companion object {
        const val API_V1_BASE = "/api/v1/abbreviations"

        private val mongoContainer = MongoDBContainer("mongo:6.0.2").withReuse(true)

        @JvmStatic
        @DynamicPropertySource
        fun applicationProperties(registry: DynamicPropertyRegistry) {
            mongoContainer.start()

            registry.add(
                "spring.data.mongodb.host"
            ) { URI.create(mongoContainer.replicaSetUrl).host }
            registry.add(
                "spring.data.mongodb.port"
            ) { URI.create(mongoContainer.replicaSetUrl).port }
            registry.add("spring.data.mongodb.replica-set-name") { "docker-rs" }
        }
    }

    @Before
    fun setup() {
        /*
            Standalone setup is not enough as we have validation in the controller level
            Reference: https://stackoverflow.com/a/49174227
         */
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    @Test
    @Order(1)
    fun testSaveAbbreviation() {
        val saveAbbreviationRequest = SaveAbbreviationRequest("TC", "Test Case", "Test Case Description")
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("${AbbreviationControllerTest.API_V1_BASE}")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveAbbreviationRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    @Order(2)
    fun testRetrieveMeaningOneResult() {
        val expectedContent = objectMapper.writeValueAsString(listOf("Test Case"))
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE?shortForm=TC")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedContent))
    }
}
