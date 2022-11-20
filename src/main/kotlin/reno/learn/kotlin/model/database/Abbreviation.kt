package reno.learn.kotlin.model.database

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Abbreviation(

    @Id
    val id: ObjectId = ObjectId.get(),
    val shortForm: String,
    val meaning: String,
    val description: String? = null,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now()
)
