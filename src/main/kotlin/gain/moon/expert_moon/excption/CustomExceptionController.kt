package gain.moon.expert_moon.excption

import gain.moon.expert_moon.controller.AuthController
import gain.moon.expert_moon.controller.ImageController
import gain.moon.expert_moon.util.ResponseFormat
import gain.moon.expert_moon.util.ResponseFormatBuilder
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@Hidden
@RestControllerAdvice()
class CustomExceptionController {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ResponseFormat<Any>> {
        return ResponseEntity
                .status(e.reason.status.value())
                .body(ResponseFormatBuilder {
                    state = e.reason.status.value()
                    message = e.reason.message
                }.noData())
    }
}