package gain.moon.expert_moon.repository

import gain.moon.expert_moon.entity.Like
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository: MongoRepository<Like, String> {
    fun findLikeByUserIdAndPostId(userId: String, postId: String): Like?
}