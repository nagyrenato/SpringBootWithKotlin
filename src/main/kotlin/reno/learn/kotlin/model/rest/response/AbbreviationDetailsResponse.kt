package reno.learn.kotlin.model.rest.response

class AbbreviationDetailsResponse(
    var shortForm: String,
    var meaning: String,
    var description: String? = "No additional description is provided"
)
