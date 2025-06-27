package gain.moon.expert_moon.service

import gain.moon.expert_moon.dto.request.FollowRequest
import gain.moon.expert_moon.dto.request.ProfilePatchRequest
import gain.moon.expert_moon.dto.response.UserResponse
import gain.moon.expert_moon.entity.User
import gain.moon.expert_moon.excption.CustomException
import gain.moon.expert_moon.excption.ExceptionState
import gain.moon.expert_moon.repository.ImageRepository
import gain.moon.expert_moon.repository.UserRepository
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class UserService(val userRepository: UserRepository, val imageRepository: ImageRepository) {
    fun patchProfile(request: ProfilePatchRequest, principal: Principal) {
        val userId = principal.name
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        imageRepository.findImageByName(request.imageName) ?: throw CustomException(ExceptionState.BAD_REQUST)
        userRepository.save(user.copy(profileImage = request.imageName))
    }
    fun follow(request: FollowRequest, principal: Principal) {
        val userId = principal.name
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        val following = userRepository.findUserById(request.userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        userRepository.save(user.copy(following = user.following.plus(request.userId)))
        userRepository.save(following.copy(followers = following.followers.plus(userId)))
        getExp(following, 100)
    }
    fun unfollow(request: FollowRequest, principal: Principal) {
        val userId = principal.name
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        val following = userRepository.findUserById(request.userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        userRepository.save(user.copy(following = user.following.minus(request.userId)))
        userRepository.save(following.copy(followers = following.followers.minus(userId)))
    }
    fun getProfile(id: String): UserResponse{
        val user = userRepository.findUserById(id) ?: throw CustomException(ExceptionState.NOT_FOUND)
        return UserResponse(
            id = user.id!!,
            name = user.name,
            level = user.level,
            currentExp = user.currentExp,
            profileImage = user.profileImage,
            followers = user.followers.size,
            following = user.following.size
        )
    }
    fun getExp(user: User, exp: Int) {
        val requiredExp = 100 + user.level * (user.level / 50)
        if (user.currentExp + exp > requiredExp) {
            val overflow = user.currentExp + exp - requiredExp
            userRepository.save(user.copy(
                level = user.level+1,
                currentExp = overflow
            ))
        }else {
            userRepository.save(user.copy(currentExp = user.currentExp + exp))
        }
    }
}