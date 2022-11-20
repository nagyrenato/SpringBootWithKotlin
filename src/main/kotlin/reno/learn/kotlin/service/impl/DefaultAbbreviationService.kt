package reno.learn.kotlin.service.impl

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.repository.AbbreviationRepository
import reno.learn.kotlin.service.AbbreviationService

@Service
class DefaultAbbreviationService(@Autowired val abbreviationRepository: AbbreviationRepository) : AbbreviationService {

    override fun retrieveMeaning(shortForm: String): Collection<String> {
        return abbreviationRepository.findByShortForm(shortForm).map { result -> result.meaning }
    }

    override fun retrieveDetails(id: String): Abbreviation? {
        return abbreviationRepository.findByIdOrNull(ObjectId(id))
    }

    override fun saveAbbreviation(saveAbbreviationRequest: SaveAbbreviationRequest) {
        val abbreviation = Abbreviation(
            shortForm = saveAbbreviationRequest.shortForm,
            meaning = saveAbbreviationRequest.meaning
        )
        abbreviationRepository.save(abbreviation)
    }

    override fun deleteAbbreviation(id: String) {
        abbreviationRepository.deleteById(ObjectId(id))
    }
}
