package gain.moon.expert_moon.dto.response

data class PostResponse(
        val id: String,
        val title: String,
        val content: String,
        val userId: String,
        val images: List<String>,
        val category: List<String>
)
