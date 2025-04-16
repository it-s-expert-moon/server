package gain.moon.expert_moon.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.LocalDateTime

@Document(collection = "post")
data class Post(
        @MongoId
        val id: String? = null,
        val title: String,
        val content: String,
        val userId: String,
        val images: List<String>,
        val category: List<String>,
        val createAt: LocalDateTime = LocalDateTime.now()
)
