package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

/**
 * Data class representing an error response.
 *
 * @property title The title of the error.
 * @property status The HTTP status code of the error.
 * @property type The type of the error.
 * @property details Additional details about the error. Can be null if no additional details are provided.
 */
data class ErrorResponse(
    @get:OpenApiExample("500")
    val statuCode: Int = 500,

    @get:OpenApiExample("POST")
    val method: String = "POST",

    @get:OpenApiExample("Server Error")
    val message: String,

    val data: Map<String, Object> = emptyMap()
)
