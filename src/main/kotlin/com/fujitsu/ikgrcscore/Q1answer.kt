package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

data class Q1answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,

    @get:OpenApiRequired
    @get:OpenApiExample("Senario1")
    val senario: String,

    @get:OpenApiRequired
    val answers: Array<Q1data>

)

data class Q1data(
    @get:OpenApiExample("Kitchen")
    val name: String,

    @get:OpenApiExample("2")
    val number: Int
)
