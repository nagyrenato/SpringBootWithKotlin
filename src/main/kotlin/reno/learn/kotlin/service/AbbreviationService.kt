package reno.learn.kotlin.service

import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.exception.AbbreviationSaveException
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest

interface AbbreviationService {

    /**
     * Retrieves a list of meanings for an abbreviation, example: RM returns ["Resource Manager"]
     */
    fun retrieveMeaning(shortForm: String): Collection<String>

    /**
     * Retrieves an abbreviation by its id
     */
    @Throws(AbbreviationNotFoundException::class)
    fun retrieveDetails(id: String): Abbreviation

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
