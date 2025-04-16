package gain.moon.expert_moon.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "user")
data class User(
        @MongoId
        val id: String? = null,
        val name: String,
        val email: String,
        val password: String,
        val level: Int = 1,
        val currentExp: Int = 0,
        val profileImage: String? = null,
        val role: String = "user",
        val following: Set<String> = emptySet(),
        val followers: Set<String> = emptySet()
)