package gain.moon.expert_moon.repository

import gain.moon.expert_moon.entity.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, String> {
    fun findRefreshTokenByUserId(userId: String): RefreshToken?
}