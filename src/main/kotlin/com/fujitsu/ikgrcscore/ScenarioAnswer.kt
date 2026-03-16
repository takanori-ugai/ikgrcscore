package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

/**
 * API response payload for a scenario lookup.
 *
 * @param statusCode HTTP-like status code returned to the client.
 * @param method HTTP method associated with the response.
 * @param message Human-readable result message.
 * @param data Scenario details returned by the endpoint.
 */
data class ScenarioAnswer(
    @get:OpenApiExample("200")
    val statusCode: Int = 200,
    @get:OpenApiExample("GET")
    val method: String = "GET",
    @get:OpenApiExample("Registration of Results is success")
    val message: String = "Succeed",
    val data: Scenario,
)

/**
 * Scenario definition returned from the scenario API.
 *
 * @param id Unique scenario identifier.
 * @param title Display title of the scenario.
 * @param scene Scene number associated with the scenario.
 * @param activities Ordered activity names included in the scenario.
 */
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
