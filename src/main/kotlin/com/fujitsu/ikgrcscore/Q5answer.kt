package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * Data class representing the answer to question 5.
 *
 * @property name The name of the user answering the question.
 * @property scenario The scenario for which the question is being answered.
 * @property answers The set of time-based room and object observations.
 */
data class Q5answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Scenario1")
    val scenario: String,
    @get:OpenApiRequired
    val answers: Set<Q5data>,
)

/**
 * Data class representing a single observation in the answer to question 5.
 *
 * @property time The timestamp of the observation.
 * @property room The room associated with the observation.
 * @property obj The set of observed objects.
 */
data class Q5data(
    @get:OpenApiExample("0000-00-00TT00:00:20.005")
    val time: String,
    @get:OpenApiExample("LivingRoom")
    val room: String,
    @get:OpenApiExample("[ \"Cup\" ]")
    val obj: Set<String>,
)
