package gain.moon.expert_moon.service

import gain.moon.expert_moon.dto.request.LikeRequest
import gain.moon.expert_moon.dto.request.PostRequest
import gain.moon.expert_moon.dto.response.PostResponse
import gain.moon.expert_moon.entity.Like
import gain.moon.expert_moon.entity.Post
import gain.moon.expert_moon.excption.CustomException
import gain.moon.expert_moon.excption.ExceptionState
import gain.moon.expert_moon.repository.ImageRepository
import gain.moon.expert_moon.repository.LikeRepository
import gain.moon.expert_moon.repository.PostRepository
import gain.moon.expert_moon.repository.UserRepository
import org.bson.Document
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service
import java.security.Principal
import java.util.*

@Service
class PostService(val postRepository: PostRepository, val userRepository: UserRepository, val imageRepository: ImageRepository, val likeRepository: LikeRepository, val mongoTemplate: MongoTemplate, val userService: UserService) {
    fun post(request: PostRequest, principal: Principal) {
        val userId = principal.name
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        request.images.forEach { imageRepository.findImageByName(it) ?: throw CustomException(ExceptionState.BAD_REQUST) }
        postRepository.save(Post(
                title = request.title,
                content = request.content,
                userId = userId,
                images = request.images,
                category = request.category
        ))
        userService.getExp(user, 20)
    }
    fun home(principal: Principal): List<PostResponse> {
        val userId = principal.name
        val user = userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)

        val matchStage = Aggregation.match(Criteria()) // 필터 필요시 조건 추가

        val lookupLikes = Aggregation.lookup("post_like", "_id", "postId", "likes")
        val lookupUser = Aggregation.lookup("user", "userId", "_id", "author")

        val likeCountField = Aggregation.addFields()
                .addFieldWithValue(
                        "likeCount",
                        Document(
                                "\$size", Document(
                                "\$ifNull", listOf("\$likes", emptyList<Any>())
                        )
                        )
                )
                .build()

        val addScore = Aggregation.addFields()
                .addField("score")
                .withValue(
                        Document(
                                "\$add",
                                listOf(
                                        // follow 가중치: 팔로우된 사용자면 30점
                                        Document(
                                                "\$cond",
                                                listOf(
                                                        Document(
                                                                "\$eq", listOf(
                                                                Document("\$arrayElemAt", listOf("\$author.followers", 0)),
                                                                userId
                                                        )
                                                        ),
                                                        30,
                                                        0
                                                )
                                        ),
                                        // like 가중치: 개당 5점
                                        Document(
                                                "\$multiply",
                                                listOf(
                                                        "\$likeCount",
                                                        5
                                                )
                                        ),
                                        // 작성 시간 가중치: 최근일수록 점수 상승 (시간 차 계산)
                                        Document(
                                                "\$multiply",
                                                listOf(
                                                        Document(
                                                                "\$divide",
                                                                listOf(
                                                                        Document(
                                                                                "\$subtract", listOf(
                                                                                System.currentTimeMillis(),
                                                                                Document("\$toLong", "\$createAt")
                                                                        )
                                                                        ),
                                                                        60000 // 분 단위
                                                                )
                                                        ),
                                                        -1
                                                )
                                        )
                                )
                        )
                )
                .build()


        val sortStage = Aggregation.sort(Sort.by(Sort.Order.desc("score")))
        val limitStage = Aggregation.limit(20)

        val aggregation = Aggregation.newAggregation(
                matchStage,
                lookupLikes,
                lookupUser,
                likeCountField,
                addScore,
                sortStage,
                limitStage
        )

        return mongoTemplate.aggregate(aggregation, "post", PostResponse::class.java).mappedResults
    }
    fun like(request: LikeRequest, principal: Principal) {
        val userId = principal.name
        userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        likeRepository.findLikeByUserIdAndPostId(userId, request.postId)?.let { throw CustomException(ExceptionState.BAD_REQUST) }
        likeRepository.save(Like(
                userId = userId,
                postId = request.postId
        ))
        val user = userRepository.findUserById(postRepository.findPostById(request.postId)!!.userId) // tlqkf
        userService.getExp(user!!, 10)
    }
    fun unlike(request: LikeRequest, principal: Principal) {
        val userId = principal.name
        userRepository.findUserById(userId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        val like = likeRepository.findLikeByUserIdAndPostId(userId, request.postId) ?: throw CustomException(ExceptionState.BAD_REQUST)
        likeRepository.delete(like)
    }
}