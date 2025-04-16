package gain.moon.expert_moon.filter

import gain.moon.expert_moon.util.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenFilter(
        @Value("\${jwt.header}") private val header: String,
        private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        try {
            val token = request.getHeader(header)
            if (!token.isNullOrEmpty()) {
                val authentication = jwtProvider.getAuthenticationByToken(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {

        }
        log.info(request.requestURI)
        filterChain.doFilter(request, response)
    }
}
