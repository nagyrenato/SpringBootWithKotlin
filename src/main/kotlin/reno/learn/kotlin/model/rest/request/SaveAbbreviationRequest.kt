package reno.learn.kotlin.model.rest.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Model for saving an abbreviation.")
data class SaveAbbreviationRequest(
    @field:Schema(
        description = "The short form for the abbreviation",
        example = "SB",
        type = "string",
        required = true
    )
    var shortForm: String,
    @field:Schema(
        description = "The meaning of the abbreviation",
        example = "Spring Boot",
        type = "string",
        required = true
    )
    var meaning: String,
    @field:Schema(
        description = "The description of the abbreviation",
        example =
        "Spring Boot makes it easy to create stand-alone, " +
            "production-grade Spring based Applications that you can \"just run\".",
        type = "string",
        required = false
    )
    var description: String?
)
