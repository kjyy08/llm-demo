package cloud.luigi99.llm.demo.user.repository

import cloud.luigi99.llm.demo.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}
