package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

/**
 * Represents an error response.
 *
 * @property statusCode The HTTP status code of the error. Default value is 500.
 * @property method The HTTP method associated with the error. Default value is "POST".
 * @property message The message of the error.
 * @property data Additional data about the error. Default value is an empty map.
 */
data class ErrorResponse(
    @get:OpenApiExample("500")
    val statuCode: Int = 500,

    @get:OpenApiExample("POST")
    val method: String = "POST",

    @get:OpenApiExample("Server Error")
    val message: String,

    val data: Map<String, Any> = emptyMap()
)
