package gain.moon.expert_moon.controller

import gain.moon.expert_moon.dto.request.FollowRequest
import gain.moon.expert_moon.dto.request.ProfilePatchRequest
import gain.moon.expert_moon.service.UserService
import gain.moon.expert_moon.util.ResponseFormat
import gain.moon.expert_moon.util.ResponseFormatBuilder
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserController(val userService: UserService) {
    @PatchMapping("/profile")
    fun patchProfile(@RequestBody @Valid request: ProfilePatchRequest, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        userService.patchProfile(request, principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/follow")
    fun follow(@RequestBody @Valid request: FollowRequest, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        userService.follow(request, principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/unfollow")
    fun unfollow(@RequestBody @Valid request: FollowRequest, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        userService.unfollow(request, principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
}