package cloud.luigi99.llm.demo.chat.dto

import java.time.LocalDateTime

data class ChatResponse(
    val id: Long,
    val threadId: Long,
    val question: String,
    val answer: String,
    val modelName: String,
    val isStreaming: Boolean,
    val createdAt: LocalDateTime
)
