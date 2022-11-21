package reno.learn.kotlin.service.impl

import io.kotlintest.matchers.collections.shouldContainAll
import io.mockk.every
import io.mockk.mockk
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.repository.AbbreviationRepository

internal class DefaultAbbreviationServiceTest {

    @Test
    fun retrieveMeaning() {
        var abbreviationRepository = mockk<AbbreviationRepository>()

        var abbreviationService = DefaultAbbreviationService(abbreviationRepository)

        every { abbreviationRepository.findByShortForm("RM") } returns
            listOf(Abbreviation(ObjectId("637b7957b3139e78227fe186"), "RM", "Resource Manager"))

        var result = abbreviationService.retrieveMeaning("RM")
        result shouldContainAll listOf("Resource Manager")
    }

    @Test
    fun retrieveDetails() {
        // TODO("Unimplemented test case")
    }

    @Test
    fun saveAbbreviation() {
        // TODO("Unimplemented test case")
    }

    @Test
    fun deleteAbbreviation() {
        // TODO("Unimplemented test case")
    }

    @Test
    fun getAbbreviationRepository() {
        // TODO("Unimplemented test case")
    }
}
