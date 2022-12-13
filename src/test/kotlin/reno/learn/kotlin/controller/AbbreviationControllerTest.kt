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
import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.model.rest.response.AbbreviationDetailsResponse
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

    /*
        Static variables
        https://www.baeldung.com/kotlin/companion-object
     */
    companion object {
        const val API_V1_BASE = "/api/v1/abbreviations"
        const val MEANING_1 = "Resource Manager"
        const val MEANING_2 = "Royal Military"
        const val SHORT_FORM_1 = "RM"
        const val TEST_CASE_ID = "637b7957b3139e78227fe188"
    }

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
        val serviceResponse = listOf(MEANING_1)
        mockitoWhen(abbreviationService.retrieveMeaning(SHORT_FORM_1)).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE?shortForm=RM")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedContent))
    }

    @Test
    fun testRetrieveMeaningMultipleResult() {
        val serviceResponse = listOf(MEANING_1, MEANING_2)
        mockitoWhen(abbreviationService.retrieveMeaning(SHORT_FORM_1)).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE?shortForm=RM")
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
        mockitoWhen(abbreviationService.retrieveMeaning(SHORT_FORM_1)).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE?shortForm=RM")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(expectedContent))
    }

    @Test
    fun testRetrieveMeaningForEmptyInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE?shortForm=")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(MockMvcResultMatchers.content().string("retrieveMeaning.value: must not be blank"))
    }

    @Test
    fun testRetrieveMeaningForBlankInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE?shortForm=")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(MockMvcResultMatchers.content().string("retrieveMeaning.value: must not be blank"))
    }

    @Test
    fun testRetrieveMeaningForMissingInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get(API_V1_BASE)
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

    @Test
    fun testRetrieveDetails() {
        val serviceResponse = AbbreviationDetailsResponse("RM", "Resource Manager", "Resource Manager Description")
        mockitoWhen(abbreviationService.retrieveDetails(TEST_CASE_ID)).thenReturn(serviceResponse)
        val expectedContent = objectMapper.writeValueAsString(serviceResponse)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE/$TEST_CASE_ID")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(expectedContent))
    }

    @Test
    fun testRetrieveDetailsNotFound() {
        mockitoWhen(
            abbreviationService.retrieveDetails(TEST_CASE_ID)
        ).thenThrow(AbbreviationNotFoundException::class.java)
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE/$TEST_CASE_ID")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(MockMvcResultMatchers.content().string("Abbreviation not found"))
    }

    @Test
    fun testRetrieveDetailsInvalidInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$API_V1_BASE/ ")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(MockMvcResultMatchers.content().string("retrieveDetails.id: must not be blank"))
    }

    /*
        By default, mocking a method without a return type does not require any additional setup, just let it go.
        Modify this behaviour if you want to throw an exception or something like this.
        https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#do_family_methods_stubs
     */
    @Test
    fun testSaveAbbreviation() {
        val saveAbbreviationRequest = SaveAbbreviationRequest("TC", "Test Case", "Test Case Description")

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(API_V1_BASE)
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveAbbreviationRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun testSaveWithInvalidBodyThatHasNoShortFormAndMeaning() {
        /*
            As SaveAbbreviationRequest is safe you cannot create an instance of it without shortForm or meaning
            But you can create a similar object
            Reference:
            https://kotlinlang.org/docs/object-declarations.html#creating-anonymous-objects-from-scratch
         */

        val saveAbbreviationRequest = object {
            val test = "Test"

            // object expressions extend Any, so `override` is required on `toString()`
            override fun toString() = test
        }

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(API_V1_BASE)
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveAbbreviationRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(
                MockMvcResultMatchers
                    .content()
                    .string(
                        "Invalid body, please refer the " +
                            "documentation at /swagger-ui/index.html#/"
                    )
            )
    }

    @Test
    fun testSaveWithInvalidBodyThatHasNoShortForm() {
        /*
            As SaveAbbreviationRequest is safe you cannot create an instance of it without shortForm or meaning
            But you can create a similar object
            Reference:
            https://kotlinlang.org/docs/object-declarations.html#creating-anonymous-objects-from-scratch
         */

        val saveAbbreviationRequest = object {
            val meaning: String = "TC"

            // object expressions extend Any, so `override` is required on `toString()`
            override fun toString() = meaning
        }

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(API_V1_BASE)
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveAbbreviationRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(
                MockMvcResultMatchers
                    .content()
                    .string(
                        "Invalid body, please refer the " +
                            "documentation at /swagger-ui/index.html#/"
                    )
            )
    }

    @Test
    fun testSaveWithInvalidBodyThatHasNoMeaning() {
        /*
            As SaveAbbreviationRequest is safe you cannot create an instance of it without shortForm or meaning
            But you can create a similar object
            Reference:
            https://kotlinlang.org/docs/object-declarations.html#creating-anonymous-objects-from-scratch
         */

        val saveAbbreviationRequest = object {
            val shortForm: String = "TC"

            // object expressions extend Any, so `override` is required on `toString()`
            override fun toString() = shortForm
        }

        mockMvc.perform(
            MockMvcRequestBuilders
                .post(API_V1_BASE)
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveAbbreviationRequest))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(
                MockMvcResultMatchers
                    .content()
                    .string(
                        "Invalid body, please refer the " +
                            "documentation at /swagger-ui/index.html#/"
                    )
            )
    }

    @Test
    fun testDeleteAbbreviation() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("$API_V1_BASE/$TEST_CASE_ID")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun testDeleteAbbreviationInvalidInput() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("$API_V1_BASE/ ")
                .accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
            .andExpect(MockMvcResultMatchers.content().string("deleteAbbreviation.id: must not be blank"))
    }
}
