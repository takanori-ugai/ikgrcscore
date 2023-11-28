package com.fujitsu.ikgrcscore

import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.config.JavalinConfig
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.staticfiles.Location
import io.javalin.openapi.*
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.OpenApiPluginConfiguration
import io.javalin.openapi.plugin.redoc.ReDocConfiguration
import io.javalin.openapi.plugin.redoc.ReDocPlugin
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration
import io.javalin.openapi.plugin.swagger.SwaggerPlugin
import io.javalin.validation.ValidationError

private val logger = KotlinLogging.logger {}

const val ISDEVSYSTEM = true
const val PORTNUMBER = 7000

/**
 * The main function to run this application.
 *
 * This function initializes the Javalin server with a custom template engine and configures various plugins
 * such as OpenApiPlugin, SwaggerPlugin, and ReDocPlugin.
 * It also sets up static file hosting and starts the server on the specified port number.
 * After starting the server, it sets up various routes for the application and logs the URLs for the ReDoc
 * and Swagger UI documentation.
 */
fun main() {
    App().app.start(PORTNUMBER)
    logger.info { "Check out ReDoc docs at http://localhost:$PORTNUMBER/redoc" }
    logger.info { "Check out Swagger UI docs at http://localhost:$PORTNUMBER/swagger-ui" }
}

class App {
    val app = Javalin.create { config: JavalinConfig ->
        val deprecatedDocsPath = "/openapi"
        config.plugins.register(
            OpenApiPlugin(
                OpenApiPluginConfiguration()
                    .withDocumentationPath(deprecatedDocsPath)
                    .withDefinitionConfiguration { _, definition ->
                        definition
                            .withOpenApiInfo { openApiInfo ->
                                openApiInfo.title = "RESTful API"
                                openApiInfo.summary = "RESTful API"
                                openApiInfo.description = "Backend API"
                                openApiInfo.version = "0.0.1"
                                openApiInfo.contact = OpenApiContact().apply {
                                    name = "API Support"
                                    url = "https://www.example.com/support"
                                    email = "ugai@fujitsu.com"
                                }

                                openApiInfo.license = OpenApiLicense().apply {
                                    name = "Apache 2.0"
                                    identifier = "Apache-2.0"
                                }
                                openApiInfo.termsOfService = "http://{host}:8081/api/v1"
                            }
                            .withServer { server ->
                                if (ISDEVSYSTEM) {
                                    server.url = "http://localhost:7000"
                                } else {
                                    server.url = "https://kgrc4si.home.kg/score"
                                }
                                server.description = "go service api server endpoint application"
                                server.addVariable(
                                    "host",
                                    "localhost",
                                    arrayOf("localhost"),
                                    "Port of the server"
                                )
                            }
                    }
            )
        )

        config.plugins.register(
            SwaggerPlugin(
                SwaggerConfiguration().apply {
                    if (!ISDEVSYSTEM) {
                        basePath = "/score"
                    }
                    uiPath = "/swagger-ui"
                    documentationPath = deprecatedDocsPath
                }
            )
        )

        config.plugins.register(
            ReDocPlugin(
                ReDocConfiguration().apply {
                    uiPath = "/redoc"
                    documentationPath = deprecatedDocsPath
                }
            )
        )

        config.staticFiles.add { staticFiles ->
            staticFiles.hostedPath = "/assets"
            staticFiles.directory = "public"
            staticFiles.location = Location.CLASSPATH
            staticFiles.precompress = false
        }

        config.plugins.enableCors { cors ->
            cors.add { it.anyHost() }
        }
    }.routes {
        get("/") { it.redirect("assets/Test0.html", HttpStatus.FOUND) }
        get("/Senario/list", this::listSenario)
        get("/Senario/{id}", this::getSenario)
        get("/Ranking", this::ranking)
        get("/Ranking/{id}", this::getRank)
        post("/Q1", this::q1)
        post("/Q2", this::q2)
        post("/Q3", this::q3)
        post("/Q4", this::q4)
        post("/Q5", this::q5)
        post("/Q6", this::q6)
        post("/Q7", this::q7)
        post("/Q8", this::q8)
    }

    /**
     * This function handles the POST request at the "/Q1" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question1",
        operationId = "Question1",
        tags = ["scoring"],
        path = "/Q1",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q1answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q1(ctx: Context) {
        val answer = ctx.bodyValidator(Q1answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q2" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question2",
        operationId = "Question2",
        tags = ["scoring"],
        path = "/Q2",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q2answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q2(ctx: Context) {
        val answer = ctx.bodyValidator(Q2answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q3" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question3",
        operationId = "Question3",
        tags = ["scoring"],
        path = "/Q3",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q3answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q3(ctx: Context) {
        val answer = ctx.bodyValidator(Q3answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q4" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question4",
        operationId = "Question4",
        tags = ["scoring"],
        path = "/Q4",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q3answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q4(ctx: Context) {
        val answer = ctx.bodyValidator(Q3answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q5" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question5",
        operationId = "Question5",
        tags = ["scoring"],
        path = "/Q5",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q5answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q5(ctx: Context) {
        val answer = ctx.bodyValidator(Q5answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q6" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question6",
        operationId = "Question6",
        tags = ["scoring"],
        path = "/Q6",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q6answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q6(ctx: Context) {
        val answer = ctx.bodyValidator(Q6answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotBlank() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q7" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question7",
        operationId = "Question7",
        tags = ["scoring"],
        path = "/Q7",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q7answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q7(ctx: Context) {
        val answer = ctx.bodyValidator(Q7answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q8" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with the user's score and ranking.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question8",
        operationId = "Question8",
        tags = ["scoring"],
        path = "/Q8",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q8answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q8(ctx: Context) {
        val answer = ctx.bodyValidator(Q8answer::class.java)
            .check({ it.name.isNotBlank() }, "Name must not be empty")
            .check({ it.senario.isNotBlank() }, "Senario must not be empty")
            .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
            .get()

        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the GET request at the "/Senario/{id}" path.
     * It retrieves a scenario based on the ID provided in the path parameter.
     *
     * @param ctx The context of the HTTP request.
     * This should include a path parameter with the ID of the scenario to retrieve.
     * @return A JSON response with the requested scenario.
     */
    @OpenApi(
        description = "Get a senario",
        deprecated = false,
        summary = "Get a senario",
        operationId = "senario",
        tags = ["senario"],
        path = "/Senario/{id}",
        pathParams = [OpenApiParam("id", String::class, "The Senario ID", false, true, example = "Senario1")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(SenarioAnswer::class)]),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun getSenario(ctx: Context) {
        val answer = ctx.pathParam("id")
        logger.info { "getSenario is called : $answer" }
        ctx.json(SenarioAnswer(data = Senario("Senario1", "Senario1", 1, listOf("Test"))))
    }

    /**
     * This function handles the GET request at the "/Senario/list" path.
     * It retrieves a list of all scenarios.
     *
     * @param ctx The context of the HTTP request.
     * @return A JSON response with a list of all scenarios.
     */
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
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun listSenario(ctx: Context) {
        logger.info { "listSenario is called" }
        ctx.json(listOf("test1", "test2"))
    }

    /**
     * This function handles GET requests to the /Ranking/{id} endpoint.
     * It retrieves the rank of a team based on the provided team ID.
     *
     * @param ctx the context object which holds information about the HTTP request and response
     */
    @OpenApi(
        description = "Get a rank",
        deprecated = false,
        summary = "Get a rank",
        operationId = "ranking",
        tags = ["ranking"],
        path = "/Ranking/{id}",
        pathParams = [OpenApiParam("id", String::class, "The Team ID", false, true, example = "TeamC")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Ranking::class)]),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun getRank(ctx: Context) {
        val answer = ctx.pathParam("id")
        logger.info { "getRank is called : $answer" }
        ctx.json(Ranking(answer, 2, 10.0))
    }

    /**
     * This function handles GET requests to the /Ranking endpoint.
     * It retrieves a list of all rankings.
     *
     * @param ctx the context object which holds information about the HTTP request and response
     */
    @OpenApi(
        description = "Get ranking list",
        deprecated = false,
        summary = "Get ranking list",
        operationId = "rankingList",
        tags = ["ranking"],
        path = "/Ranking",
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(RankingList::class)]),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun ranking(ctx: Context) {
        logger.info { "ranking is called." }
        ctx.json(RankingList(listOf(Ranking("TeamB", 2, 10.0))))
    }
}
