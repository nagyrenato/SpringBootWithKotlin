package reno.learn.kotlin.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import reno.learn.kotlin.service.AbbreviationService

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
internal class AbbreviationControllerTest {

    private val abbreviationService: AbbreviationService = mockk()

    val objectMapper = ObjectMapper()

    @InjectMocks
    var controllerUnderTest: AbbreviationController = AbbreviationController(abbreviationService)

    lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        // this must be called for the @Mock annotations above to be processed
        // and for the mock service to be injected into the controller under
        // test.
        MockitoAnnotations.openMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build()
    }

    @Test
    fun testRetrieveMeaningOneResult() {
        val serviceResponse = listOf("Resource Manager")
        every { abbreviationService.retrieveMeaning("RM") } returns serviceResponse
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
        every { abbreviationService.retrieveMeaning("RM") } returns serviceResponse
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
        every { abbreviationService.retrieveMeaning("RM") } returns serviceResponse
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
        val serviceResponse = listOf<String>()
        every { abbreviationService.retrieveMeaning("RM") } returns serviceResponse
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/abbreviations")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(expectedContent))
    }
}
