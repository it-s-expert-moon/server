package gain.moon.expert_moon.excption

import org.springframework.http.HttpStatus

enum class ExceptionState(val status: HttpStatus = HttpStatus.OK, val message: String) {
    BAD_REQUST(HttpStatus.BAD_REQUEST, "you bad"),
    EMAIL_IN_USE(HttpStatus.BAD_REQUEST, "email in use")
}
