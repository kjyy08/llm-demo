package cloud.luigi99.llm.demo.chat.domain.entity

import cloud.luigi99.llm.demo.thread.domain.entity.Thread
import cloud.luigi99.llm.demo.user.domain.entity.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "chats")
@EntityListeners(AuditingEntityListener::class)
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    val thread: Thread,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @field:NotBlank(message = "질문은 필수입니다")
    @Column(nullable = false, columnDefinition = "TEXT")
    val question: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val answer: String,

    @Column(name = "model_name")
    val modelName: String,

    @Column(name = "is_streaming")
    val isStreaming: Boolean = false,

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
}
