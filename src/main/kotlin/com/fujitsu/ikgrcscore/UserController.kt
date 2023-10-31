package com.fujitsu.ikgrcscore

import io.javalin.http.Context
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiContent
import io.javalin.openapi.OpenApiParam
import io.javalin.openapi.OpenApiRequestBody
import io.javalin.openapi.OpenApiResponse

/**
 * Controller for user-related operations.
 */
internal object UserController {

    /**
     * Creates a new user.
     *
     * @param ctx The context of the request.
     */
    @OpenApi(
        summary = "Create a new user",
        description = "Test service for Javalin",
        tags = ["User"],
        path = "/users",
        methods = [HttpMethod.POST],
        requestBody = OpenApiRequestBody([OpenApiContent(NewUserRequest::class)]),
        responses = [
            OpenApiResponse("200", [OpenApiContent(NewUserRequest::class)])
        ]
    )
    fun create(ctx: Context) {
        val newUserRequest = ctx.bodyAsClass(NewUserRequest::class.java)
        println(newUserRequest.email)
        println(newUserRequest.name)
        val response = NewUserRequest("Test1", "test@gmail.com")
        ctx.json(response)
    }

    /**
     * Retrieves all users.
     *
     * @param ctx The context of the request.
     */
    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        path = "/users",
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(String::class)])
        ]
    )
    fun getAll(ctx: Context) {
        ctx.html("<H1>Test</H1>")
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param ctx The context of the request.
     */
    @OpenApi(
        summary = "Get one user",
        operationId = "getOneUser",
        tags = ["User"],
        path = "/users/{userId}",
        pathParams = [OpenApiParam("userId", Int::class, "The user ID", false, true, example = "1")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(String::class)])
        ]
    )
    fun getOne(ctx: Context) {
        ctx.html("<H1>Test</H1>")
    }

    /**
     * Updates a user by ID.
     *
     * @param ctx The context of the request.
     */
    @OpenApi(
        summary = "Update a user",
        operationId = "updateUser",
        tags = ["User"],
        requestBody = OpenApiRequestBody([OpenApiContent(NewUserRequest::class)]),
        path = "/users/{userId}",
        pathParams = [OpenApiParam("userId", Int::class, "The user ID", false, true)],
        methods = [HttpMethod.PATCH],
        responses = [
            OpenApiResponse("201"),
            OpenApiResponse("400", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun update(ctx: Context) {
        ctx.html("<H1>Test</H1>")
    }

    /**
     * Deletes a user by ID.
     *
     * @param ctx The context of the request.
     */
    @OpenApi(
        summary = "Delete a user",
        operationId = "deleteUser",
        tags = ["User"],
        path = "/users/{userId}",
        pathParams = [OpenApiParam("userId", Int::class, "The user ID", false, true)],
        methods = [HttpMethod.DELETE],
        responses = [
            OpenApiResponse("200", [OpenApiContent(String::class)])
        ]
    )
    fun delete(ctx: Context) {
        ctx.html("<H1>Test</H1>")
    }
}
