package gain.moon.expert_moon.dto.response

data class UserResponse(
    val id: String,
    val name: String,
    val level: Int,
    val currentExp: Int,
    val profileImage: String? = null,
    val followers: Int,
    val following: Int
)
