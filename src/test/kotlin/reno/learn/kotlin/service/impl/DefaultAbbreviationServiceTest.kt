package reno.learn.kotlin.service.impl

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.bson.types.ObjectId
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import reno.learn.kotlin.exception.InvalidRequestException
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.repository.AbbreviationRepository

internal class DefaultAbbreviationServiceTest : BehaviorSpec({

    val resultShortForm1 = "RM"
    val resultMeaning1 = "Resource Manager"
    val resultObjectIdHex1 = "637b7957b3139e78227fe186"
    val resultDescription = "Manager who is responsible for resources"

    val resultMeaning2 = "Royal Military"
    val resultObjectIdHex2 = "637b7957b3139e78227fe187"

    val expectedShortForm = "RM"
    val expectedMeaning1 = "Resource Manager"
    val expectedObjectIdHex = "637b7957b3139e78227fe186"
    val expectedDescription = "Manager who is responsible for resources"

    val expectedMeaning2 = "Royal Military"

    val testCaseShortForm = "RM"
    val testCaseId = "637b7957b3139e78227fe188"

    Given("a service") {
        var abbreviationRepository = mockk<AbbreviationRepository>()
        var abbreviationService = DefaultAbbreviationService(abbreviationRepository)

        When("the service is being called for meanings and has one result") {
            every { abbreviationRepository.findByShortForm(resultShortForm1) } returns
                listOf(Abbreviation(ObjectId(resultObjectIdHex1), resultShortForm1, resultMeaning1))
            Then("it returns a list of possible meanings") {
                var result = abbreviationService.retrieveMeaning(testCaseShortForm)
                result shouldContainExactlyInAnyOrder listOf(expectedMeaning1)
            }
        }

        When("the service is being called for meanings and has multiple results") {
            every { abbreviationRepository.findByShortForm(resultShortForm1) } returns
                listOf(
                    Abbreviation(ObjectId(resultObjectIdHex1), resultShortForm1, resultMeaning1),
                    Abbreviation(ObjectId(resultObjectIdHex2), resultShortForm1, resultMeaning2)
                )
            Then("it returns a list of possible meanings") {
                var result = abbreviationService.retrieveMeaning(testCaseShortForm)
                result shouldContainExactlyInAnyOrder listOf(expectedMeaning1, expectedMeaning2)
            }
        }

        When("the service is being called for meanings and has no result") {
            every { abbreviationRepository.findByShortForm(resultShortForm1) } returns
                listOf()
            Then("it returns a list of possible meanings") {
                var result = abbreviationService.retrieveMeaning(testCaseShortForm)
                result.size shouldBe 0
            }
        }

        When("the service is being called for meanings but the short form is empty") {
            Then("it returns a list of possible meanings") {
                var result = abbreviationService.retrieveMeaning("")
                result.size shouldBe 0
            }
        }

        When("the service is being called for meanings but the short form is blank") {
            Then("it returns a list of possible meanings") {
                var result = abbreviationService.retrieveMeaning(" ")
                result.size shouldBe 0
            }
        }

        When("the service is being called for details and has a result") {
            every { abbreviationRepository.findByIdOrNull(ObjectId(testCaseId)) } returns
                Abbreviation(ObjectId(resultObjectIdHex1), resultShortForm1, resultMeaning1, resultDescription)
            Then("it returns a list of possible meanings") {
                var result = abbreviationService.retrieveDetails(testCaseId)
                result.id shouldBe ObjectId(expectedObjectIdHex)
                result.shortForm shouldBe expectedShortForm
                result.meaning shouldBe expectedMeaning1
                result.description shouldBe expectedDescription
            }
        }

        When("the service is being called for details but the id is empty") {
            Then("it returns a list of possible meanings") {
                assertThrows(InvalidRequestException::class.java) {
                    abbreviationService.retrieveDetails("")
                }
            }
        }

        When("the service is being called for details but the id is blank") {
            Then("it returns a list of possible meanings") {
                assertThrows(InvalidRequestException::class.java) {
                    abbreviationService.retrieveDetails("")
                }
            }
        }

        When("the service is being called for saving") {
            val slot1 = slot<Abbreviation>()
            every { abbreviationRepository.save(capture(slot1)) } returns mockk()

            Then("it returns a list of possible meanings") {
                val saveRequest: SaveAbbreviationRequest = SaveAbbreviationRequest(
                    shortForm = "TC",
                    meaning = "Test Case",
                    description = null
                )

                abbreviationService.saveAbbreviation(saveRequest)
                slot1.captured.shortForm shouldBe "TC"
            }
        }
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
})
