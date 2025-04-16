package gain.moon.expert_moon.controller

import gain.moon.expert_moon.dto.response.ImagePostResponse
import gain.moon.expert_moon.service.ImageService
import gain.moon.expert_moon.util.ResponseFormat
import gain.moon.expert_moon.util.ResponseFormatBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/image")
class ImageController(val imageService: ImageService) {
    @PostMapping(consumes = ["multipart/form-data"])
    fun postImage(@RequestParam("image") image: MultipartFile): ResponseEntity<ResponseFormat<ImagePostResponse>> {
        val result = imageService.saveImage(image)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
}