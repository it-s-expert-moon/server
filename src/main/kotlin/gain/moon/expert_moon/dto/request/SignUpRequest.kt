package gain.moon.expert_moon.dto.request

data class SignUpRequest(
        val name: String,
        val email: String,
        val randomCode: String,
        val password: String,
)
