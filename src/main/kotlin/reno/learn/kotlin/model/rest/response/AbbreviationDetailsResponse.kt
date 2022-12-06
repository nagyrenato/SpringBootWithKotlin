package reno.learn.kotlin.model.rest.response

class AbbreviationDetailsResponse(
    val shortForm: String,
    val meaning: String,
    val description: String? = "No additional description is provided"
)
