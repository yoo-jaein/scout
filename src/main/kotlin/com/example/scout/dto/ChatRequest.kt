package com.example.scout.dto

data class ChatRequest(
    val userMessage: String,
    val conversationId: String? = null,
    val userId: String? = null
)
