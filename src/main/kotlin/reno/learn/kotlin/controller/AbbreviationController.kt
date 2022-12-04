package reno.learn.kotlin.controller

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reno.learn.kotlin.model.database.Abbreviation
import reno.learn.kotlin.model.rest.request.SaveAbbreviationRequest
import reno.learn.kotlin.model.rest.response.AbbreviationDetailsResponse
import reno.learn.kotlin.service.AbbreviationService
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
@RequestMapping("/api/v1")
@Validated
class AbbreviationController(@Autowired private var abbreviationService: AbbreviationService) {

    @ApiOperation(value = "Get possible meanings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Successful retrieval",
                response = String::class,
                responseContainer = "List"
            ),
            ApiResponse(code = 500, message = "Server error"),
        ]
    )
    @GetMapping("/abbreviations", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun retrieveMeaning(
        @NotEmpty
        @Valid
        @RequestParam(name = "shortForm", required = true)
        value: String
    ):
        Collection<String> {
        return abbreviationService.retrieveMeaning(value)
    }

    @ApiOperation(value = "Get details", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Successful retrieval",
                response = AbbreviationDetailsResponse::class
            ),
            ApiResponse(code = 500, message = "Server error"),
        ]
    )
    @GetMapping("/abbreviations/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun retrieveDetails(
        @ApiParam(value = "id")
        @PathVariable(
            "id",
            required = true
        )
        id: String
    ):
        AbbreviationDetailsResponse {
        var serviceResult: Abbreviation = abbreviationService.retrieveDetails(id)
        return AbbreviationDetailsResponse(
            shortForm = serviceResult.shortForm,
            meaning = serviceResult.meaning,
            description = serviceResult.description
        )
    }

    @ApiOperation(value = "Save abbreviation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
        value = [
            ApiResponse(
                code = 201,
                message = "Successful save"
            ),
            ApiResponse(code = 500, message = "Server error"),
        ]
    )
    @PostMapping("/abbreviations", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun saveAbbreviation(@RequestBody saveAbbreviationRequest: SaveAbbreviationRequest) {
        return abbreviationService.saveAbbreviation(saveAbbreviationRequest)
    }

    @ApiOperation(value = "Delete abbreviation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
        value = [
            ApiResponse(
                code = 204,
                message = "Successful save"
            ),
            ApiResponse(code = 500, message = "Server error"),
        ]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/abbreviations/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteAbbreviation(@PathVariable("id", required = true) id: String) {
        return abbreviationService.deleteAbbreviation(id)
    }
}
