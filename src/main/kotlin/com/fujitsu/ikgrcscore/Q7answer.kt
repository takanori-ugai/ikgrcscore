package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * Data class representing the answer to question 7.
 *
 * @property name The name of the user answering the question.
 * @property scenario The scenario for which the question is being answered.
 * @property answers The set of spatial relationships submitted as the answer.
 */
data class Q7answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Scenario1")
    val scenario: String,
    @get:OpenApiRequired
    val answers: Set<Q7data>,
)

/**
 * Data class representing a single spatial relationship in the answer to question 7.
 *
 * @property obj1 The reference object in the relationship.
 * @property obj2 The target object in the relationship.
 * @property relation The relation between the two objects.
 */
data class Q7data(
    @get:OpenApiExample("Table")
    val obj1: String,
    @get:OpenApiExample("Cup")
    val obj2: String,
    @get:OpenApiExample("ON")
    val relation: String,
)
