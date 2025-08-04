package cloud.luigi99.llm.demo.chat.repository

import cloud.luigi99.llm.demo.chat.domain.entity.Chat
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Long> {

    fun findByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): List<Chat>

    fun findByThreadIdOrderByCreatedAtAsc(threadId: Long): List<Chat>
}
