package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * Data class representing the answer to question 2.
 *
 * @property name The name of the user answering the question.
 * @property scenario The scenario for which the question is being answered.
 * @property answers The set of object-count pairs submitted for the answer.
 */
data class Q2answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Scenario1")
    val scenario: String,
    @get:OpenApiRequired
    @get:OpenApiExample("[ { \"name\": \"WALK\",   \"number\": 1 } ]")
    val answers: Set<Q2data>,
)

/**
 * Data class representing one counted item in the answer to question 2.
 *
 * @property name The name of the counted item.
 * @property number The count associated with the item.
 */
data class Q2data(
    val name: String,
    val number: Int,
)
