package gain.moon.expert_moon.dto.request

data class PostRequest(
        val title: String,
        val content: String,
        val images: List<String>,
        val category: List<String>
)
