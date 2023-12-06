package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

data class Q7answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Senario1")
    val senario: String,
    @get:OpenApiRequired
    val answers: List<Q7data>,
)

data class Q7data(
    @get:OpenApiExample("Table")
    val obj1: String,
    @get:OpenApiExample("Cup")
    val obj2: String,
    @get:OpenApiExample("ON")
    val relation: String,
)
