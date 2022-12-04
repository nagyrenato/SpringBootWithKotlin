package reno.learn.kotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import reno.learn.kotlin.service.AbbreviationService
import org.mockito.Mockito.`when` as mockitoWhen

/*
    SpringRunner is responsible to provide the WebApplicationContext
 */
@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
internal class AbbreviationControllerTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    /*
       Instead of the default implementation we mock the service
     */
    @MockBean
    private lateinit var abbreviationService: AbbreviationService

    private val objectMapper = ObjectMapper()

    private lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        /*
            Standalone setup is not enough as we have validation in the controller level
            Reference: https://stackoverflow.com/a/49174227
         */
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun testRetrieveMeaningOneResult() {
        val serviceResponse = listOf("Resource Manager", "Royal Military")
        mockitoWhen(abbreviationService.retrieveMeaning("RM")).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/abbreviations?shortForm=RM")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedContent))
    }

    @Test
    fun testRetrieveMeaningMultipleResult() {
        val serviceResponse = listOf("Resource Manager", "Royal Military")
        mockitoWhen(abbreviationService.retrieveMeaning("RM")).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/abbreviations?shortForm=RM")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(expectedContent))
    }

    @Test
    fun testRetrieveMeaningNoResult() {
        val serviceResponse = listOf<String>()
        mockitoWhen(abbreviationService.retrieveMeaning("RM")).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/abbreviations?shortForm=RM")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(expectedContent))
    }

    @Test
    fun testRetrieveMeaningForInvalidInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/abbreviations?shortForm=")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(MockMvcResultMatchers.content().string("retrieveMeaning.value: must not be empty"))
    }

    @Test
    fun testRetrieveMeaningForMissingInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/abbreviations")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(
                MockMvcResultMatchers.content()
                    .string(
                        "Required request parameter 'shortForm' " +
                            "for method parameter type String is not present"
                    )
            )
    }
}
