package gain.moon.expert_moon.service

import gain.moon.expert_moon.dto.response.ImagePostResponse
import gain.moon.expert_moon.entity.Image
import gain.moon.expert_moon.repository.ImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

@Service
class ImageService(@Value("\${image.folder}") val location: String, val imageRepository: ImageRepository) {
    fun saveImage(image: MultipartFile): ImagePostResponse {
        val id = UUID.randomUUID().toString() + image.originalFilename
        val file = File(location+"/"+id)
        image.transferTo(file)
        imageRepository.save(Image(name = id))
        return ImagePostResponse(id)
    }
}