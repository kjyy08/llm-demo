package cloud.luigi99.llm.demo.llm.model

import cloud.luigi99.llm.demo.llm.enums.AIProvider
import dev.langchain4j.model.chat.ChatModel
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ChatModelFactory (
    @Value("\${langchain4j.google-ai-gemini.chat-model.api-key}")
    val geminiApiKey: String
){
    fun createChatModel(provider: AIProvider, modelName: String) = when(provider) {
        AIProvider.GOOGLE -> createGeminiModel(modelName)
//        AIProvider.OPENAI -> createOpenAiModel(modelName) TODO: openai 모델 추가 시 구현
        else -> throw IllegalArgumentException("Provider not supported")
    }

    private fun createGeminiModel(modelName: String): ChatModel {
        return GoogleAiGeminiChatModel.builder()
            .apiKey(geminiApiKey)
            .modelName(modelName)
            .build()
    }
}