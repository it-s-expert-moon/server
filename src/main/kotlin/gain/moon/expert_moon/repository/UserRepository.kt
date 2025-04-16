package gain.moon.expert_moon.repository

import gain.moon.expert_moon.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, String> {
    fun findUserByEmail(email: String): User?
    fun findUserByName(name: String): User?
    fun findUserById(id: String): User?
}