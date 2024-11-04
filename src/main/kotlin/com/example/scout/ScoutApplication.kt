package com.example.scout

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class ScoutApplication

fun main(args: Array<String>) {
    runApplication<ScoutApplication>(*args)
}
