package cloud.luigi99.llm.demo.user.domain.dto

import cloud.luigi99.llm.demo.user.enums.Role

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val role: Role
)
