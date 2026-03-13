package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample
import io.javalin.openapi.OpenApiRequired

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

data class Q2data(
    val name: String,
    val number: Int,
)
