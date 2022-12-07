package reno.learn.kotlin.model.database

import org.bson.types.ObjectId
import org.junit.Assert.assertEquals
import org.junit.Test
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

    /*
        TODO("This is not a perfect solution, only the number of invocation is tested")
     */
    @Test
    fun testDefaultId() {
        val objectId = ObjectId("637b7957b3139e78227fe188")

        mockStatic(ObjectId::class.java).use { objectIdStatic ->
            objectIdStatic.`when`<Any> { ObjectId.get() }
                .thenReturn(objectId)

            val localAbbreviation = Abbreviation(
                shortForm = "TC",
                meaning = "Test Case",
                description = "Description"
            )

            objectIdStatic.verify(
                { ObjectId.get() },
                times(1)
            )
        }
    }

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

    /*
        This test mocks the return of the Instant
     */
    @Test
    fun testCreatedDate() {
        val instantNow = Instant.ofEpochSecond(1670415376)

        mockStatic(Instant::class.java).use { instantStatic ->
            instantStatic.`when`<Any> { Instant.now() }
                .thenReturn(instantNow)

            val localAbbreviation = Abbreviation(
                ObjectId("637b7957b3139e78227fe188"),
                "TC",
                "Test Case",
                "Description"
            )

            instantStatic.verify(
                { Instant.now() },
                times(1)
            )

            val expectedEpochSeconds: Long = 1670415376
            assertEquals(expectedEpochSeconds, localAbbreviation.createdDate)
        }
    }
}
