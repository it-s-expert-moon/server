package gain.moon.expert_moon.dto.request

data class RefreshRequest(
        val accessToken: String,
        val refreshToken: String
)
