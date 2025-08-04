package cloud.luigi99.llm.demo.user.domain.entity

import cloud.luigi99.llm.demo.user.enums.Role
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    
    @Column(unique = true, nullable = false)
    val email: String,
    
    @Column(nullable = false)
    val password: String,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.MEMBER
) {
    companion object {
        fun create(
            email: String,
            password: String,
            name: String,
            role: Role = Role.MEMBER
        ): User {
            return User(
                email = email,
                password = password,
                name = name,
                role = role
            )
        }
    }
}