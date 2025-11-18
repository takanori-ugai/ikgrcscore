package com.fujitsu.ikgrcscore

import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.Context
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiContent
import io.javalin.openapi.OpenApiParam
import io.javalin.openapi.OpenApiResponse
import io.javalin.validation.ValidationError

private val logger = KotlinLogging.logger {}

class SenarioHandler {
    @OpenApi(
        description = "Get a senario",
        deprecated = false,
        summary = "Get a senario",
        operationId = "senario",
        tags = ["senario"],
        path = "/Senario/{id}",
        pathParams = [OpenApiParam("id", String::class, "The Senario ID", false, true, example = "Scenario1")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(SenarioAnswer::class)]),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)]),
        ],
    )
    fun getSenario(ctx: Context) {
        val answer = ctx.pathParam("id")
        logger.info { "getSenario is called : $answer" }
        ctx.json(SenarioAnswer(data = Senario("Senario1", "Senario1", 1, listOf("Test"))))
    }

    @OpenApi(
        description = "Get episodes",
        deprecated = false,
        summary = "Get episodes",
        operationId = "senarioList",
        tags = ["senario"],
        path = "/Senario/list",
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Array<String>::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)]),
        ],
    )
    fun listSenario(ctx: Context) {
        logger.info { "listSenario is called" }
        ctx.json(listOf("test1", "test2"))
    }
}
