package gain.moon.expert_moon.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
                .formLogin { it.disable() }
                .csrf { it.disable() }
                .cors { it.disable() }
                .authorizeHttpRequests {
                    it
                            .requestMatchers("/**").permitAll()
                }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .build()
    }
}