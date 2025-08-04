package cloud.luigi99.llm.demo.chat.controller

import cloud.luigi99.llm.demo.chat.dto.CustomChatRequest
import cloud.luigi99.llm.demo.chat.dto.ChatResponse
import cloud.luigi99.llm.demo.chat.service.ChatService
import jakarta.validation.Valid
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/chat")
class ChatController(
    private val chatService: ChatService
) {
    @GetMapping
    fun getChatHistory(
        authentication: Authentication,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<ChatResponse> {
        val userId = authentication.name.toLong()
        return chatService.getChatHistory(userId, page, size)
    }

    @PostMapping
    fun createChat(
        @Valid @RequestBody request: CustomChatRequest,
        authentication: Authentication
    ): ChatResponse {
        val userId = authentication.name.toLong()
        return chatService.createChat(request, userId)
    }
}
