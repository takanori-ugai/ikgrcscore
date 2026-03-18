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

/**
 * Handles scenario-related API endpoints.
 */
class ScenarioHandler {
    /**
     * Returns a scenario.
     *
     * NOTE: This is a scaffold implementation and currently returns a fixed scenario,
     * ignoring the ID path parameter.
     *
     * @param ctx Javalin request context containing the scenario ID.
     */
    @OpenApi(
        description = "Get a scenario",
        deprecated = false,
        summary = "Get a scenario",
        operationId = "scenario",
        tags = ["scenario"],
        path = "/Scenario/{id}",
        pathParams = [OpenApiParam("id", String::class, "The Scenario ID", false, true, example = "Scenario1")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(ScenarioAnswer::class)]),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)]),
        ],
    )
    fun getScenario(ctx: Context) {
        val answer = ctx.pathParam("id")
        logger.info { "getScenario is called : $answer" }
        ctx.json(ScenarioAnswer(data = Scenario("Scenario1", "Scenario1", 1, listOf("Test"))))
    }

    /**
     * Returns the available scenario IDs.
     *
     * @param ctx Javalin request context for writing the JSON response.
     */
    @OpenApi(
        description = "Get episodes",
        deprecated = false,
        summary = "Get episodes",
        operationId = "scenarioList",
        tags = ["scenario"],
        path = "/Scenario/list",
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Array<String>::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)]),
        ],
    )
    fun listScenario(ctx: Context) {
        logger.info { "listScenario is called" }
        ctx.json(listOf("test1", "test2"))
    }
}
