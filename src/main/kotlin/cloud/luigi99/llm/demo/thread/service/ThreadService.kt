package cloud.luigi99.llm.demo.thread.service

import cloud.luigi99.llm.demo.chat.dto.ChatResponse
import cloud.luigi99.llm.demo.chat.repository.ChatRepository
import cloud.luigi99.llm.demo.thread.domain.entity.Thread
import cloud.luigi99.llm.demo.thread.repository.ThreadRepository
import cloud.luigi99.llm.demo.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ThreadService(
    val threadRepository: ThreadRepository,
    val chatRepository: ChatRepository,
    val userRepository: UserRepository
) {

    fun getChatsByThreadId(threadId: Long, userId: Long): List<ChatResponse> {
        validateThreadAccess(threadId, userId)

        return chatRepository.findByThreadIdOrderByCreatedAtAsc(threadId)
            .map { chat ->
                ChatResponse(
                    id = chat.id!!,
                    threadId = chat.thread.id!!,
                    question = chat.question,
                    answer = chat.answer,
                    modelName = chat.modelName,
                    isStreaming = chat.isStreaming,
                    createdAt = chat.createdAt
                )
            }
    }

    fun createNewThread(userId: Long): Thread {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val thread = Thread(user = user)
        return threadRepository.save(thread)
    }

    fun getOrCreateThread(threadId: Long, userId: Long): Thread {
        val existingThread = threadRepository.findById(threadId)
            .orElseGet { createNewThread(userId) }

        if (existingThread.user.id != userId) {
            throw IllegalArgumentException("해당 스레드에 접근 권한이 없습니다")
        }

        val thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30)
        
        return if (existingThread.updatedAt.isBefore(thirtyMinutesAgo)) {
            createNewThread(userId)
        } else {
            existingThread
        }
    }

    fun updateLastModifiedTime(threadId: Long) {
        threadRepository.findById(threadId).ifPresent { thread ->
            thread.updatedAt = LocalDateTime.now()
            threadRepository.save(thread)
        }
    }

    fun validateThreadAccess(threadId: Long, userId: Long) {
        val thread = threadRepository.findById(threadId)
            .orElseThrow { IllegalArgumentException("스레드를 찾을 수 없습니다") }

        if (thread.user.id != userId) {
            throw IllegalArgumentException("해당 스레드에 접근 권한이 없습니다")
        }
    }
}
