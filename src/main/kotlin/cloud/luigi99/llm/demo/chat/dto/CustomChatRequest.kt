package cloud.luigi99.llm.demo.chat.dto

import cloud.luigi99.llm.demo.llm.enums.AIProvider
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CustomChatRequest(
    @field:NotBlank(message = "질문은 필수입니다")
    val prompt: String,
    @field:NotNull(message = "제공자를 선택하세요")
    val provider: AIProvider,
    @field:NotBlank(message = "모델 선택은 필수입니다")
    val modelName: String,
    val isStreaming: Boolean = false,
    val threadId: Long? = null
)
