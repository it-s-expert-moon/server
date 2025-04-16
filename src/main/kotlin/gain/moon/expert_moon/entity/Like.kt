package gain.moon.expert_moon.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "post_like")
data class Like(
        @MongoId
        val id: String? = null,
        val userId: String,
        val postId: String
)
