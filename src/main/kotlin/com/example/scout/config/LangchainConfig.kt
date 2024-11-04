package com.example.scout.config

import com.example.scout.service.SearchService
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.service.AiServices
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LangchainConfig {
    @Bean
    fun chatLanguageModel(@Value("\${openai.api-key}") apiKey: String): ChatLanguageModel {
        return OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName("gpt-4o-mini")
            .temperature(0.7)
            .build()
    }

    @Bean
    fun agent(
        chatLanguageModel: ChatLanguageModel,
        searchService: SearchService
    ): Agent {
        return AiServices.builder(Agent::class.java)
            .chatLanguageModel(chatLanguageModel)
            .tools(searchService)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .build()
    }
}
