package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

data class Q8answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Senario1")
    val senario: String,
    @get:OpenApiRequired
    val answers: Set<Q8data>,
)

data class Q8data(
    @get:OpenApiExample("Table")
    val name: String,
    @get:OpenApiExample("Cup")
    val change: Set<Q8element>,
)

data class Q8element(
    @get:OpenApiExample("[ 1.1, 2.5, 3.2]")
    val place: List<Double>,
    @get:OpenApiExample("[\"ON\", \"CLEAN\"]")
    val status: Set<String>,
)
