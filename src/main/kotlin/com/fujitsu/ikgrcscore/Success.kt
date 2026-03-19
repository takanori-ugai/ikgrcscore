package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

/**
 * Response payload returned when result registration succeeds.
 *
 * @property statusCode HTTP-like status code returned to the client.
 * @property method HTTP method associated with the response.
 * @property message Human-readable success message.
 * @property data Score and ranking details returned by the API.
 */
data class Success(
    @get:OpenApiExample("200")
    val statusCode: Int = 200,
    @get:OpenApiExample("POST")
    val method: String = "POST",
    @get:OpenApiExample("Registration of Results is success")
    val message: String = "Succeed",
    val data: SuccessData,
)

/**
 * Score payload returned inside a successful registration response.
 *
 * @property score The calculated score for the submission.
 * @property ranking The ranking assigned to the submission.
 */
data class SuccessData(
    @get:OpenApiExample("0.3")
    val score: Double,
    @get:OpenApiExample("2")
    val ranking: Int,
)
