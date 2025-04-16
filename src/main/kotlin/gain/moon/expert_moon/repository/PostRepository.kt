package gain.moon.expert_moon.repository

import gain.moon.expert_moon.entity.Post
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepository: MongoRepository<Post, String> {
}