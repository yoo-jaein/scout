package com.example.scout.config

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage

interface Agent {
    @SystemMessage("""
        당신은 도움이 되는 AI 어시스턴트입니다.
        질문에 답하기 위해 웹 검색을 활용할 수 있습니다.
        검색 결과를 바탕으로 정확하고 도움되는 답변을 제공하세요.
    """)
    @UserMessage("{{it}}")
    fun chat(message: String): String
}
