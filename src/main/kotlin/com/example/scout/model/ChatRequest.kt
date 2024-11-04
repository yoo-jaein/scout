package com.example.scout.model

data class ChatRequest(
    val userMessage: String,
    val conversationId: String? = null,
    val userId: String? = null
)
