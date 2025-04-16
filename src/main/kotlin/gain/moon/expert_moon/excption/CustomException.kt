package gain.moon.expert_moon.excption

class CustomException(val reason: ExceptionState): RuntimeException(reason.message)