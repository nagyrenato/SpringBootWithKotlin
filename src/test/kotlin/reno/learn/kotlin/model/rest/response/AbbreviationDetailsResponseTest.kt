package reno.learn.kotlin.model.rest.response

import org.junit.Assert.assertEquals
import org.junit.Test

class AbbreviationDetailsResponseTest {

    private val abbreviationDetailsResponse = AbbreviationDetailsResponse("TC", "Test Case", "Description")

    @Test
    fun testShortForm() {
        assertEquals("TC", abbreviationDetailsResponse.shortForm)
    }

    @Test
    fun testMeaning() {
        assertEquals("Test Case", abbreviationDetailsResponse.meaning)
    }

    @Test
    fun testDescription() {
        assertEquals("Description", abbreviationDetailsResponse.description)
    }

    @Test
    fun testDefaultDescription() {
        val abbreviationDetailsResponse = AbbreviationDetailsResponse("TC", "Test Case")
        assertEquals("No additional description is provided", abbreviationDetailsResponse.description)
    }
}
