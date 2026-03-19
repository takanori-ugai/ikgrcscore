package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * Data class representing the answer to question 6.
 *
 * @property name The name of the user answering the question.
 * @property scenario The scenario for which the question is being answered.
 * @property answers The action label submitted as the answer.
 */
data class Q6answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Scenario1")
    val scenario: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Grab")
    val answers: String,
)
