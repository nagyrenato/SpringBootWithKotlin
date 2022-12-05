package reno.learn.kotlin.service.impl

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.exception.InvalidRequestException
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.model.rest.response.AbbreviationDetailsResponse
import reno.learn.kotlin.repository.AbbreviationRepository
import reno.learn.kotlin.service.AbbreviationService

@Service
class DefaultAbbreviationService(
    @Autowired private val abbreviationRepository: AbbreviationRepository
) : AbbreviationService {

    override fun retrieveMeaning(shortForm: String): Collection<String> {
        if (shortForm.isBlank()) {
            throw InvalidRequestException()
        }
        return abbreviationRepository.findByShortForm(shortForm).map { result -> result.meaning }
    }

    override fun retrieveDetails(id: String): AbbreviationDetailsResponse {
        id.validateAsId()
        val result = abbreviationRepository.findByIdOrNull(ObjectId(id))
        return if (result != null) {
            AbbreviationDetailsResponse(
                shortForm = result.shortForm,
                meaning = result.meaning,
                description = result.description
            )
        } else {
            throw AbbreviationNotFoundException()
        }
    }

    override fun saveAbbreviation(saveAbbreviationRequest: SaveAbbreviationRequest) {
        if (saveAbbreviationRequest.shortForm.isBlank() || saveAbbreviationRequest.meaning.isBlank()) {
            throw InvalidRequestException()
        }

        val abbreviation = Abbreviation(
            shortForm = saveAbbreviationRequest.shortForm,
            meaning = saveAbbreviationRequest.meaning,
            description = saveAbbreviationRequest.description
        )
        abbreviationRepository.save(abbreviation)
    }

    override fun deleteAbbreviation(id: String) {
        id.validateAsId()
        abbreviationRepository.deleteById(ObjectId(id))
    }

    fun String.validateAsId() {
        if (this.isBlank()) {
            throw InvalidRequestException()
        }
    }
}
