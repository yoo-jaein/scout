package com.example.scout.service

import com.example.scout.config.Agent
import com.example.scout.dto.ChatRequest
import com.example.scout.dto.ChatResponse
import com.example.scout.model.Conversation
import com.example.scout.repository.ConversationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ChatService(
    private val agent: Agent,
    private val conversationRepository: ConversationRepository
) {
    suspend fun processChat(request: ChatRequest): ChatResponse = coroutineScope {
        val conversation = getOrCreateConversation(request.conversationId)

        // Langchain4j 에이전트를 통한 응답 생성
        val response = withContext(Dispatchers.IO) {
            agent.chat(request.userMessage)
        }

        // 대화 저장
        saveConversation(conversation, request.userMessage, response)

        ChatResponse(
            response = response,
            conversationId = conversation.conversationId
        )
    }

    private suspend fun getOrCreateConversation(conversationId: String?): Conversation {
        return conversationId?.let {
            conversationRepository.findByConversationId(it)
        } ?: Conversation(
            conversationId = UUID.randomUUID().toString()
        )
    }

    private suspend fun saveConversation(
        conversation: Conversation,
        userMessage: String,
        assistantResponse: String
    ) {
        conversation.messages.addAll(
            listOf(
                ChatMessage(
                    role = Role.USER,
                    content = userMessage
                ),
                ChatMessage(
                    role = Role.ASSISTANT,
                    content = assistantResponse
                )
            )
        )
        conversationRepository.save(conversation)
    }
}

enum class Role {
    USER, ASSISTANT, SYSTEM
}

data class ChatMessage(
    val role: Role,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
