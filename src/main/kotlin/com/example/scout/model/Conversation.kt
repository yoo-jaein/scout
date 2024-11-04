package com.example.scout.model

import com.example.scout.service.ChatMessage
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "conversations")
data class Conversation(
    @Id
    val id: String = ObjectId().toString(),
    val conversationId: String,
    val messages: MutableList<ChatMessage> = mutableListOf(),
    val createdAt: Instant = Instant.now()
)
