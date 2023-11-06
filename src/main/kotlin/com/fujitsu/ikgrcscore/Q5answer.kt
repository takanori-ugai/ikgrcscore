package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

data class Q5answer(
    @get:OpenApiRequired
    @get:OpenApiExample("Takanori Ugai")
    val name: String,

    @get:OpenApiRequired
    @get:OpenApiExample("Senario1")
    val senario: String,

    @get:OpenApiRequired
    val answers: List<Q5data>

)
data class Q5data(
    @get:OpenApiExample("0000-00-00TT00:00:20.005")
    val time: String,
    @get:OpenApiExample("LivingRoom")
    val room: String,
    @get:OpenApiExample("[ \"Cup\" ]")
    val obj: List<String>
)
