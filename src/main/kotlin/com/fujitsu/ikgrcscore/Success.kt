package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

data class Success(
    @get:OpenApiExample("200")
    val statusCode: Int = 200,
    @get:OpenApiExample("POST")
    val method: String = "POST",
    @get:OpenApiExample("Registration of Results is success")
    val message: String = "Succeed",
    val data: SuccessData,
)

data class SuccessData(
    @get:OpenApiExample("0.3")
    val score: Double,
    @get:OpenApiExample("2")
    val ranking: Int,
)
