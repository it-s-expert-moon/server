package gain.moon.expert_moon.controller

import gain.moon.expert_moon.dto.request.*
import gain.moon.expert_moon.dto.response.LoginResponse
import gain.moon.expert_moon.service.AuthService
import gain.moon.expert_moon.util.ResponseFormat
import gain.moon.expert_moon.util.ResponseFormatBuilder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {
    val log = LoggerFactory.getLogger(javaClass)
    @PostMapping("/check/email")
    fun checkEmail(@RequestBody @Valid request: CheckEmailRequest, httpServletRequest: HttpServletRequest): ResponseEntity<ResponseFormat<Any>> {
        authService.checkEmail(request, httpServletRequest.session)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid request: SignUpRequest, httpServletRequest: HttpServletRequest): ResponseEntity<ResponseFormat<Any>> {
        authService.signUp(request, httpServletRequest.session)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest, httpServletResponse: HttpServletResponse): ResponseEntity<ResponseFormat<LoginResponse>> {
        val result = authService.login(request)
        httpServletResponse.setHeader("Authorization", result.accessToken)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @PostMapping("/refresh")
    fun refresh(@RequestBody @Valid request: RefreshRequest, httpServletResponse: HttpServletResponse): ResponseEntity<ResponseFormat<LoginResponse>> {
        val result = authService.refresh(request)
        httpServletResponse.setHeader("Authorization", result.accessToken)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }

}