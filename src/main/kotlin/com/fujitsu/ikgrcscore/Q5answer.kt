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
    @get:OpenApiExample("[ { \"time\": \"0000-00-00TT00:00:20.005\",   \"room\": \"Living Room\", \"obj\": \"Cup\", \"number\": 1 } ]")
    val answers: Array<Q5data>

)
data class Q5data(
    val name: String,
    val change: String,
    val obj: String,
    val number: Int
)
