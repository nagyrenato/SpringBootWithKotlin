package reno.learn.kotlin.controller

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import reno.learn.kotlin.service.AbbreviationService


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class AbbreviationControllerTest {

    private val abbreviationService: AbbreviationService = mockk()

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
    fun testGet() {
        every { abbreviationService.retrieveMeaning("RM") } returns
            listOf("Resource Manager")

        mockMvc.get("/api/v1/abbreviations?shortForm=RM") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.ALL
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { "[Resource Manager" }
        }
    }

}
