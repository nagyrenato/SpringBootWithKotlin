package reno.learn.kotlin.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import reno.learn.kotlin.model.database.Abbreviation
import java.util.*

@Repository
interface AbbreviationRepository : MongoRepository<Abbreviation, ObjectId> {

    @Query("{'shortForm':'?0'}")
    fun findByShortForm(shortForm: String): List<Abbreviation>
}
