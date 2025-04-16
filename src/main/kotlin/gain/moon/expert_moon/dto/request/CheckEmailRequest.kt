package gain.moon.expert_moon.dto.request

import jakarta.validation.constraints.Email

data class CheckEmailRequest(
        @Email
        val email: String
)
