package com.example.scout.controller

import com.example.scout.dto.ChatRequest
import com.example.scout.dto.ChatResponse
import com.example.scout.service.ChatService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatService: ChatService) {

    @PostMapping
    suspend fun chat(@RequestBody request: ChatRequest): ChatResponse {
        return chatService.processChat(request)
    }
}
