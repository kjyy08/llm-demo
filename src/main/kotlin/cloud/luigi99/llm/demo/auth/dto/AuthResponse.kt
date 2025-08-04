package cloud.luigi99.llm.demo.auth.dto

import cloud.luigi99.llm.demo.user.domain.dto.UserResponse

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val user: UserResponse
)