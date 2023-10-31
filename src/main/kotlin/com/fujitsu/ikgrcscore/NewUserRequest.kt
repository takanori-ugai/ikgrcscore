package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiDescription
import io.javalin.openapi.OpenApiExample

/**
 * Data class representing a request to create a new user.
 *
 * @property name The name of the new user.
 * @property email The email of the new user.
 */
data class NewUserRequest(
    @get:OpenApiDescription("The name of the new user.")
    @get:OpenApiExample("John Doe")
    val name: String,

    @get:OpenApiDescription("The email of the new user.")
    @get:OpenApiExample("john.doe@example.com")
    val email: String
)
