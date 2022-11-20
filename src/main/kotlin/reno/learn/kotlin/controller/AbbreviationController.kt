package reno.learn.kotlin.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reno.learn.kotlin.exception.AbbreviationNotFoundException
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.model.rest.response.AbbreviationDetailsResponse
import reno.learn.kotlin.service.AbbreviationService

@RestController
@RequestMapping("/api/v1")
class AbbreviationController(@Autowired var abbreviationService: AbbreviationService) {

    @GetMapping("/abbreviations")
    fun retrieveMeaning(@RequestParam("value") value: String): Collection<String> {
        return abbreviationService.retrieveMeaning(value)
    }

    @GetMapping("/abbreviations/{id}")
    fun retrieveDetails(@PathVariable("id") id: String): AbbreviationDetailsResponse {
        var serviceResult: Abbreviation =
            abbreviationService.retrieveDetails(id) ?: throw AbbreviationNotFoundException()
        return AbbreviationDetailsResponse(
            shortForm = serviceResult.shortForm,
            meaning = serviceResult.meaning,
            description = serviceResult.description
        )
    }

    @PostMapping("/abbreviations")
    fun saveAbbreviation(@RequestBody saveAbbreviationRequest: SaveAbbreviationRequest) {
        return abbreviationService.saveAbbreviation(saveAbbreviationRequest)
    }

    @DeleteMapping("/abbreviations/{id}")
    fun deleteAbbreviation(@PathVariable("id") id: String) {
        return abbreviationService.deleteAbbreviation(id)
    }
}
