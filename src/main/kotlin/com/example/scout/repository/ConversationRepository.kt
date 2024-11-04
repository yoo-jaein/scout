package com.example.scout.repository

import com.example.scout.model.Conversation
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversationRepository : CoroutineCrudRepository<Conversation, String> {
    suspend fun findByConversationId(conversationId: String): Conversation?
}
