package reno.learn.kotlin.service

import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.exception.AbbreviationSaveException
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.model.rest.response.AbbreviationDetailsResponse

/*
    This class is responsible for the connection between the presentation and persistence layer.
    It knows about the response types and the db types too. Controllers and repositories shouldn't know anything
    about their data classes.
 */
interface AbbreviationService {

    /**
     * Retrieves a list of meanings for an abbreviation, example: RM returns ["Resource Manager"]
     */
    fun retrieveMeaning(shortForm: String): Collection<String>

    /**
     * Retrieves an abbreviation by its id
     */
    @Throws(AbbreviationNotFoundException::class)
    fun retrieveDetails(id: String): AbbreviationDetailsResponse

    /**
     * Saves an abbreviation to the permanent store
     */
    @Throws(AbbreviationSaveException::class)
    fun saveAbbreviation(saveAbbreviationRequest: SaveAbbreviationRequest)

    /**
     * Deletes an abbreviation from the permanent store
     */
    fun deleteAbbreviation(id: String)
}
