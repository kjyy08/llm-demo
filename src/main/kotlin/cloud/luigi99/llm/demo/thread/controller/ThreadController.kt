package cloud.luigi99.llm.demo.thread.controller

import cloud.luigi99.llm.demo.chat.dto.ChatResponse
import cloud.luigi99.llm.demo.thread.service.ThreadService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/thread")
class ThreadController (
    val threadService: ThreadService
){
    @GetMapping("/{threadId}")
    fun getChatsByThread(
        @PathVariable threadId: Long,
        authentication: Authentication
    ): List<ChatResponse> {
        val userId = authentication.name.toLong()
        return threadService.getChatsByThreadId(threadId, userId)
    }
}