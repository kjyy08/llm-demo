package cloud.luigi99.llm.demo.llm.service

import cloud.luigi99.llm.demo.llm.enums.AIProvider
import cloud.luigi99.llm.demo.llm.model.ChatModelFactory
import dev.langchain4j.model.chat.ChatModel
import org.springframework.stereotype.Service

@Service
class AssistantService(
    val chatModelFactory: ChatModelFactory
) {
    fun createAssistant(provider: AIProvider, modelName: String): ChatModel {
        return chatModelFactory.createChatModel(provider, modelName)
    }
}