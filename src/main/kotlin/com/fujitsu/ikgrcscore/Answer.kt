package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * A data class that encapsulates an answer with a generic type parameter [Ans].
 *
 * @param name The name of the individual associated with the answer, marked as required by OpenAPI.
 * @param scenario The scenario description, marked as required by OpenAPI.
 * @param answers The actual answers, which can be of any type represented by [Ans], marked as required by OpenAPI.
 */
data class Answer<Ans>(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Scenario1")
    val scenario: String,
    @get:OpenApiRequired
    @get:OpenApiExample("[ \"WALK\",   \"GRAB\" ]")
    val answers: Ans,
)
