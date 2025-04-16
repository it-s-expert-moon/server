package gain.moon.expert_moon.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "refresh_token")
data class RefreshToken(
        @MongoId
        val id: String? = null,
        val token: String,
        val userId: String
)
