package reno.learn.kotlin.model.database

import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.times
import java.time.Instant

class AbbreviationTest {

    private val abbreviation = Abbreviation(
        ObjectId("637b7957b3139e78227fe188"),
        "TC",
        "Test Case",
        "Description"
    )

    @Test
    fun testId() {
        assertEquals(ObjectId("637b7957b3139e78227fe188"), abbreviation.id)
    }

    @Test
    fun testShortForm() {
        assertEquals("TC", abbreviation.shortForm)
    }

    @Test
    fun testMeaning() {
        assertEquals("Test Case", abbreviation.meaning)
    }

    @Test
    fun testDescription() {
        assertEquals("Description", abbreviation.description)
    }

    @Test
    fun testCreatedDate() {
        val instantNow = Instant.now()

        mockStatic(Instant::class.java).use { instantStatic ->
            instantStatic.`when`<Any>(MockedStatic.Verification { Instant.now() })
                .thenReturn(instantNow)

            Abbreviation(
                ObjectId("637b7957b3139e78227fe188"),
                "TC",
                "Test Case",
                "Description"
            )

            instantStatic.verify(
                { Instant.now() },
                times(1)
            )
        }
    }
}
