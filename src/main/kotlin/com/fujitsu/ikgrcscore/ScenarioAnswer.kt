package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

data class ScenarioAnswer(
    @get:OpenApiExample("200")
    val statusCode: Int = 200,
    @get:OpenApiExample("GET")
    val method: String = "GET",
    @get:OpenApiExample("Registration of Results is success")
    val message: String = "Succeed",
    val data: Scenario,
)

data class Scenario(
    @get:OpenApiExample("Scenario1")
    val id: String,
    @get:OpenApiExample("Scenario1")
    val title: String,
    @get:OpenApiExample("1")
    val scene: Int,
    @get:OpenApiExample("[ \"Read_book1_scene1\" ]")
    val activities: List<String>,
)
