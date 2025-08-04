package cloud.luigi99.llm.demo.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:Email(message = "유효한 이메일 형식이 아닙니다")
    @field:NotBlank(message = "이메일은 필수입니다")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    val password: String
)
