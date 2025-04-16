package gain.moon.expert_moon.util

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import kotlin.io.encoding.Base64

@Component
class JwtProvider(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.refresh-token-expires-time}") val refreshTokenExpiresTime: Long,
    @Value("\${jwt.access-token-expires-time}") val accessTokenExpiresTime: Long
): InitializingBean {
    lateinit var key: Key
    @OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)
    override fun afterPropertiesSet() {
        val keyByte = Base64.decode(secret)
        this.key = Keys.hmacShaKeyFor(keyByte)
    }
    val log = LoggerFactory.getLogger(javaClass)
    fun createAccessToken(id: String, role: String): String {
        val date = Date(System.currentTimeMillis() + accessTokenExpiresTime)
        return Jwts.builder()
            .setSubject(id)
            .claim("role", role)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(date)
            .compact()
    }
    fun createRefreshToken(id: String): String {
        val date = Date(System.currentTimeMillis() + refreshTokenExpiresTime)
        return Jwts.builder()
            .setSubject(id)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(date)
            .compact()
    }

    fun getAuthenticationByToken(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val role = listOf(SimpleGrantedAuthority(claims["role"].toString()))
        val principal = User(claims.subject, "", role)
        return UsernamePasswordAuthenticationToken(principal, token, role)
    }
    fun getClaimsByExpiredToken(token: String): Claims {
        return try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

}