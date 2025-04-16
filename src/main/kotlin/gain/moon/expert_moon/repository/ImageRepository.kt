package gain.moon.expert_moon.repository

import gain.moon.expert_moon.entity.Image
import org.springframework.data.mongodb.repository.MongoRepository

interface ImageRepository: MongoRepository<Image, String> {
    fun findImageByName(name: String): Image?
}