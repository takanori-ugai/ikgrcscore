package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

/**
 * Data class representing the answer to question 1.
 *
 * @property name The name of the user answering the question.
 * @property senario The scenario for which the question is being answered.
 * @property answers An array of data objects representing the user's answers.
 */
data class Q1answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,

    @get:OpenApiRequired
    @get:OpenApiExample("Senario1")
    val senario: String,

    @get:OpenApiRequired
    val answers: List<Q1data>

)

/**
 * Data class representing a single answer in the array of answers for question 1.
 *
 * @property name The name of the item being answered about.
 * @property number The number associated with the item.
 */
data class Q1data(
    @get:OpenApiExample("Kitchen")
    val name: String,

    @get:OpenApiExample("2")
    val number: Int
)
