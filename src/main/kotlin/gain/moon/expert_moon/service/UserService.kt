package gain.moon.expert_moon.service

import gain.moon.expert_moon.dto.request.FollowRequest
import gain.moon.expert_moon.dto.request.ProfilePatchRequest
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
    }
    fun unfollow(request: FollowRequest, principal: Principal) {
        val userId = principal.name
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        val following = userRepository.findUserById(request.userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        userRepository.save(user.copy(following = user.following.minus(request.userId)))
        userRepository.save(following.copy(followers = following.followers.minus(userId)))
    }
}