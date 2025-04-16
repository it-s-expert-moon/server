package gain.moon.expert_moon.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "image")
data class Image(
        @MongoId
        val id: String? = null,
        val name: String
)
