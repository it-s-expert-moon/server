package gain.moon.expert_moon.controller

import gain.moon.expert_moon.dto.request.CheckEmailRequest
import gain.moon.expert_moon.dto.request.LikeRequest
import gain.moon.expert_moon.dto.request.PostRequest
import gain.moon.expert_moon.dto.response.PostResponse
import gain.moon.expert_moon.service.PostService
import gain.moon.expert_moon.util.ResponseFormat
import gain.moon.expert_moon.util.ResponseFormatBuilder
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/post")
class PostController(val postService: PostService) {
    @PostMapping
    fun post(@RequestBody @Valid request: PostRequest, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        postService.post(request, principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping("/home")
    fun home(principal: Principal): ResponseEntity<ResponseFormat<List<PostResponse>>> {
        val result = postService.home(principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }

    @PostMapping("/like")
    fun like(request: LikeRequest, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        postService.like(request, principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/unlike")
    fun unlike(request: LikeRequest, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        postService.unlike(request, principal)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
}