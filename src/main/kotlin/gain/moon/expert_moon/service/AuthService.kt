package gain.moon.expert_moon.service

import gain.moon.expert_moon.dto.request.*
import gain.moon.expert_moon.dto.response.LoginResponse
import gain.moon.expert_moon.entity.RefreshToken
import gain.moon.expert_moon.entity.User
import gain.moon.expert_moon.excption.CustomException
import gain.moon.expert_moon.excption.ExceptionState
import gain.moon.expert_moon.repository.ImageRepository
import gain.moon.expert_moon.repository.RefreshTokenRepository
import gain.moon.expert_moon.repository.UserRepository
import gain.moon.expert_moon.util.JwtProvider
import jakarta.servlet.http.HttpSession
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class AuthService(val userRepository: UserRepository, val emailService: EmailService, val passwordEncoder: BCryptPasswordEncoder, val jwtProvider: JwtProvider, val refreshTokenRepository: RefreshTokenRepository, val imageRepository: ImageRepository) {
    fun checkEmail(request: CheckEmailRequest, session: HttpSession) {
        userRepository.findUserByEmail(request.email)?.let { throw CustomException(ExceptionState.BAD_REQUST) }
        val randomCode = (1..4).map { "0123456789".random() }.joinToString("")
        emailService.sendRandomCode(request.email, randomCode)
        session.setAttribute("email", request.email)
        session.setAttribute("randomCode", randomCode)
        session.maxInactiveInterval = 360
    }
    fun signUp(request: SignUpRequest, session: HttpSession) {
        userRepository.findUserByEmail(request.email)?.let { throw CustomException(ExceptionState.BAD_REQUST) }
        userRepository.findUserByName(request.name)?.let { throw CustomException(ExceptionState.BAD_REQUST) }
        try {
            if (!session.getAttribute("email").equals(request.email)
                    || !session.getAttribute("randomCode").equals(request.randomCode))
                throw CustomException(ExceptionState.BAD_REQUST)
        } catch (e: Exception) {
            throw CustomException(ExceptionState.BAD_REQUST)
        }
        val user = User(
                email = request.email,
                name = request.name,
                password = passwordEncoder.encode(request.password)
        )
        userRepository.save(user)
    }
    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findUserByEmail(request.email) ?: throw CustomException(ExceptionState.BAD_REQUST)
        if (!passwordEncoder.matches(request.password, user.password)) throw CustomException(ExceptionState.BAD_REQUST)
        val accessToken = jwtProvider.createAccessToken(user.id!!, user.role)
        val refreshToken: RefreshToken = refreshTokenRepository.findRefreshTokenByUserId(user.id!!) ?: run {
            val sRefreshToken = RefreshToken(
                    token = jwtProvider.createRefreshToken(user.id!!),
                    userId = user.id!!
            )
            refreshTokenRepository.save(sRefreshToken)
        }
        return LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken.token
        )
    }
    fun refresh(request: RefreshRequest): LoginResponse {
        val userId = jwtProvider.getClaimsByExpiredToken(request.accessToken).subject
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        val refreshToken = refreshTokenRepository.findRefreshTokenByUserId(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        if(refreshToken.token != request.refreshToken) throw CustomException(ExceptionState.BAD_REQUST)
        val accessToken = jwtProvider.createAccessToken(userId, user.role)
        val newRefreshToken = jwtProvider.createRefreshToken(userId)
        refreshTokenRepository.save(refreshToken.copy(token = newRefreshToken))
        return LoginResponse(
                accessToken = accessToken,
                refreshToken = newRefreshToken
        )
    }
}