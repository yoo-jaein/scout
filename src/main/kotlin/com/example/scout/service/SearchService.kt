package com.example.scout.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import dev.langchain4j.agent.tool.Tool
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service

@Service
class SearchService(
    @Value("\${daum.api-key}") private val apiKey: String,
) {
    private val client = OkHttpClient()
    private val objectMapper = ObjectMapper()

    @Tool("Search for information. Input should be a search query string.")
    fun search(query: String, maxResults: Int = 3): String {
        try {
            val req = Request.Builder()
                .url(
                    HttpUrl.Builder()
                        .scheme("https")
                        .host("dapi.kakao.com")
                        .addPathSegments("v2/search/web")
                        .addQueryParameter("query", query)
                        .build()
                )
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK $apiKey")
                .build()

            println("검색 중. . . $query")

            client.newCall(req).execute().use { response ->
                if (!response.isSuccessful) throw Exception("Unexpected response: ${response.code}")
                val responseString = response.body?.string() ?: throw Exception("No response body")
                val responseJson = objectMapper.readTree(responseString)

                println(responseJson)

                val searchResponse = objectMapper.convertValue(responseJson, SearchResponse::class.java)

                return searchResponse.documents
                    .take(maxResults)
                    .joinToString("\n\n") { doc ->
                        """
                        제목: ${doc.title}
                        내용: ${doc.contents}
                        날짜: ${doc.datetime}
                        링크: ${doc.url}
                        """.trimIndent()
                    }
            }
        } catch (e: Exception) {
            return "검색 중 오류가 발생했습니다: ${e.message}"
        }
    }
}


data class SearchResponse(
    @JsonProperty("documents") val documents: List<Document> = emptyList(),
    @JsonProperty("meta") val meta: Meta
)

data class Document @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("contents") val contents: String,
    @JsonProperty("datetime") val datetime: String
)


data class Meta(
    @JsonProperty("is_end") val is_end: Boolean,
    @JsonProperty("pageable_count") val pageable_count: Int,
    @JsonProperty("total_count") val total_count: Int
)
