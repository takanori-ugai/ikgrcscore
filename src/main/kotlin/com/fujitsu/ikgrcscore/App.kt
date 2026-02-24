package com.fujitsu.ikgrcscore

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.config.JavalinConfig
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.staticfiles.Location
import io.javalin.openapi.HttpMethod
import io.javalin.openapi.OpenApi
import io.javalin.openapi.OpenApiContent
import io.javalin.openapi.OpenApiParam
import io.javalin.openapi.OpenApiRequestBody
import io.javalin.openapi.OpenApiResponse
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.redoc.ReDocPlugin
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
    App().javalinApp.start(PORTNUMBER)
    logger.info { "Check out ReDoc docs at http://localhost:$PORTNUMBER/redoc" }
    logger.info { "Check out Swagger UI docs at http://localhost:$PORTNUMBER/swagger-ui" }
}

class App {
    private val conf = HikariConfig()

    init {
        conf.jdbcUrl = "jdbc:sqlite:test.db"
    }

    val ds = HikariDataSource(conf)
    val sql = "select * from table1"
    val javalinApp =
        Javalin.create { config: JavalinConfig ->
            val deprecatedDocsPath = "/openapi"
            config.registerPlugin(
                OpenApiPlugin { pluginConfig ->
                    pluginConfig.withDefinitionConfiguration { _, definition ->
                        definition.info { openApiInfo ->
                            openApiInfo.title("RESTful API")
                            openApiInfo.summary("RESTful API")
                            openApiInfo.description("Backend API")
                            openApiInfo.version("0.0.1")
                            openApiInfo.withContact { contact ->
                                contact.name("API Support")
                                contact.url("https://www.example.com/support")
                                contact.email("ugai@fujitsu.com")
                            }
                            openApiInfo.withLicense { license ->
                                license.name("Apache 2.0")
                                license.identifier("Apache-2.0")
                            }
                            openApiInfo.termsOfService("http://{host}:8081/api/v1")
                        }.server { server ->
                            if (ISDEVSYSTEM) {
                                server.url("http://localhost:7000")
                            } else {
                                server.url("https://kgrc4si.home.kg/score")
                            }
                            server.description("go service api server endpoint application")
                            server.variable(
                                key = "host",
                                description = "Port of the server",
                                defaultValue = "localhost",
                                "localhost",
                            )
                        }
                    }
                },
            )

            config.registerPlugin(
                SwaggerPlugin { swaggerConfiguration ->
                    if (!ISDEVSYSTEM) {
                        swaggerConfiguration.basePath = "/score"
                    }
                    swaggerConfiguration.uiPath = "/swagger-ui"
                    swaggerConfiguration.documentationPath = deprecatedDocsPath
                },
            )

            config.registerPlugin(
                ReDocPlugin { redocConfig ->
                    redocConfig.uiPath = "/redoc"
                    redocConfig.documentationPath = deprecatedDocsPath
                },
            )

            config.staticFiles.add { staticFiles ->
                staticFiles.hostedPath = "/assets"
                staticFiles.directory = "public"
                staticFiles.location = Location.CLASSPATH
                staticFiles.precompress = false
            }

            config.bundledPlugins.enableCors { cors ->
                cors.addRule {
                    it.anyHost()
                }
            }

            config.router.apiBuilder {
                val senarioHandler = SenarioHandler()
                get("/") { it.redirect("assets/Test0.html", HttpStatus.FOUND) }
                get("/Senario/list", senarioHandler::listSenario)
                get("/Senario/{id}", senarioHandler::getSenario)
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q1answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q1(ctx: Context) {
        ds.connection.use { con ->
            con.prepareStatement(sql).use { ps ->
                ps.executeQuery().use { rs ->
                    // 実行結果を標準出力
                    while (rs.next()) {
                        logger.info { "${rs.getString(1)} | ${rs.getString(2)}" }
                    }
                }
            }
        }
        val answer =
            ctx.bodyValidator(Q1answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q2answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q2(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q2answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q3answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q3(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q3answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q3answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q4(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q3answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q5answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q5(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q5answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q6answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q6(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q6answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotBlank() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q7answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q7(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q7answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
        requestBody =
            OpenApiRequestBody(
                required = true,
                content = [OpenApiContent(Q8answer::class)],
            ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ValidationError::class)], description = "Error in Input"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server"),
        ],
    )
    fun q8(ctx: Context) {
        val answer =
            ctx.bodyValidator(Q8answer::class.java)
                .check({ it.name.isNotBlank() }, "Name must not be empty")
                .check({ it.senario.isNotBlank() }, "Senario must not be empty")
                .check({ it.answers.isNotEmpty() }, "Answers must not be empty")
                .get()

        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(DEFAULT_SCORE, DEFAULT_RANK)))
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
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)]),
        ],
    )
    fun getRank(ctx: Context) {
        val answer = ctx.pathParam("id")
        logger.info { "getRank is called : $answer" }
        ctx.json(Ranking(answer, DEFAULT_RANK, DEFAULT_SCORE))
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
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)]),
        ],
    )
    fun ranking(ctx: Context) {
        logger.info { "ranking is called." }
        ctx.json(RankingList(listOf(Ranking("TeamB", DEFAULT_RANK, DEFAULT_SCORE))))
    }

    companion object {
        const val DEFAULT_SCORE = 0.3
        const val DEFAULT_RANK = 3
    }
}
