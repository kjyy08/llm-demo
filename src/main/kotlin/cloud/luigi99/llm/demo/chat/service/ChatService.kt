package cloud.luigi99.llm.demo.chat.service

import cloud.luigi99.llm.demo.chat.domain.entity.Chat
import cloud.luigi99.llm.demo.chat.dto.CustomChatRequest
import cloud.luigi99.llm.demo.chat.dto.ChatResponse
import cloud.luigi99.llm.demo.chat.repository.ChatRepository
import cloud.luigi99.llm.demo.llm.service.AssistantService
import cloud.luigi99.llm.demo.thread.service.ThreadService
import cloud.luigi99.llm.demo.user.repository.UserRepository
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.request.ChatRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    val chatRepository: ChatRepository,
    val threadService: ThreadService,
    val userRepository: UserRepository,
    val assistantService: AssistantService
) {

    @Transactional
    fun createChat(request: CustomChatRequest, userId: Long): ChatResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }

        val thread = if (request.threadId != null) {
            threadService.getOrCreateThread(request.threadId, userId)
        } else {
            threadService.createNewThread(userId)
        }

        val previousChats = chatRepository.findByThreadIdOrderByCreatedAtAsc(thread.id!!)
        val messages = mutableListOf<ChatMessage>()

        previousChats.forEach { chat ->
            messages.add(UserMessage(chat.question))
            messages.add(AiMessage(chat.answer))
        }

        messages.add(UserMessage(request.prompt))

        val chatRequest = ChatRequest.builder()
            .messages(messages)
            .build()

        val model = assistantService.createAssistant(
            request.provider,
            request.modelName
        )

        val result = model.chat(chatRequest)

        val chat = Chat(
            thread = thread,
            user = user,
            question = request.prompt,
            answer = result.aiMessage().text(),
            modelName = request.modelName,
            isStreaming = request.isStreaming
        )

        val savedChat = chatRepository.save(chat)
        threadService.updateLastModifiedTime(thread.id!!)

        return ChatResponse(
            id = savedChat.id!!,
            threadId = thread.id!!,
            question = savedChat.question,
            answer = savedChat.answer,
            modelName = savedChat.modelName,
            isStreaming = savedChat.isStreaming,
            createdAt = savedChat.createdAt
        )
    }

    fun getChatHistory(userId: Long, page: Int, size: Int): List<ChatResponse> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))

        return chatRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
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
}
