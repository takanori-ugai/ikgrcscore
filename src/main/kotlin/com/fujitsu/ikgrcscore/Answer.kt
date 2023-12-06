package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

data class Answer<Ans>(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,
    @get:OpenApiRequired
    @get:OpenApiExample("Senario1")
    val senario: String,
    @get:OpenApiRequired
    @get:OpenApiExample("[ \"WALK\",   \"GRAB\" ]")
    val answers: Ans,
)
