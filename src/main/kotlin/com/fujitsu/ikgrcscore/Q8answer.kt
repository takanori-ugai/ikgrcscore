package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * Data class representing the answer to question 8.
 *
 * @property name The name of the user answering the question.
 * @property scenario The scenario for which the question is being answered.
 * @property answers The set of objects and their reported changes.
 */
data class Q8answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Scenario1")
    val scenario: String,
    @get:OpenApiRequired
    val answers: Set<Q8data>,
)

/**
 * Data class representing a changed object in the answer to question 8.
 *
 * @property name The name of the object that changed.
 * @property change The set of changes reported for the object.
 */
data class Q8data(
    @get:OpenApiExample("Table")
    val name: String,
    @get:OpenApiExample("Cup")
    val change: Set<Q8element>,
)

/**
 * Data class representing one change entry for an object in question 8.
 *
 * @property place The reported coordinates associated with the change.
 * @property status The set of status values associated with the change.
 */
data class Q8element(
    @get:OpenApiExample("[ 1.1, 2.5, 3.2]")
    val place: List<Double>,
    @get:OpenApiExample("[\"ON\", \"CLEAN\"]")
    val status: Set<String>,
)
