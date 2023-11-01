package com.fujitsu.ikgrcscore

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.resolve.DirectoryCodeResolver
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.Javalin
import io.javalin.config.JavalinConfig
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import io.javalin.http.staticfiles.Location
import io.javalin.openapi.*
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.OpenApiPluginConfiguration
import io.javalin.openapi.plugin.redoc.ReDocConfiguration
import io.javalin.openapi.plugin.redoc.ReDocPlugin
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration
import io.javalin.openapi.plugin.swagger.SwaggerPlugin
import io.javalin.rendering.template.JavalinJte
import java.nio.file.Path

private val logger = KotlinLogging.logger {}
fun main() {
    App.main()
}

/**
 * A sample application of Javalin.
 *
 * This class has no useful logic; it's just a documentation example.
 *
 * @param T the type of a member in this group.
 * @property name the name of this group.
 * @constructor Creates an empty group.
 */
object App {
    const val isDevSystem = false
    const val portNumber = 7000

    /**
     * The main function to run this application.
     *
     */
    fun main() {
        JavalinJte.init(createTemplateEngine()) { isDevSystem }
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
                                    server.url = "http://localhost:7000/"
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
        }.start(portNumber)

        app.get("/") { it.redirect("assets/Test0.html", HttpStatus.FOUND) }
        app.get("/Senario/list", this::listSenario)
        app.get("/Senario/{id}", this::getSenario)
        app.post("/Q1", this::q1)
        app.post("/Q2", this::q2)
        app.post("/Q3", this::q3)
        app.post("/Q4", this::q4)
        app.post("/Q5", this::q5)
        app.post("/Q6", this::q6)
        app.post("/Q7", this::q7)
        app.post("/Q8", this::q8)

        logger.info { "Check out ReDoc docs at http://localhost:$portNumber/redoc" }
        logger.info { "Check out Swagger UI docs at http://localhost:$portNumber/swagger-ui" }
    }

    /**
     * This function handles the POST request at the "/Q1" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question1",
        operationId = "Question1",
        tags = ["user"],
        path = "/Q1",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q1answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q1(ctx: Context) {
        val answer = ctx.bodyAsClass<Q1answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q2" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question2",
        operationId = "Question2",
        tags = ["user"],
        path = "/Q2",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q2answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q2(ctx: Context) {
        val answer = ctx.bodyAsClass<Q2answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q3" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question3",
        operationId = "Question3",
        tags = ["user"],
        path = "/Q3",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q3answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q3(ctx: Context) {
        val answer = ctx.bodyAsClass<Q3answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q4" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question4",
        operationId = "Question4",
        tags = ["user"],
        path = "/Q4",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q3answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q4(ctx: Context) {
        val answer = ctx.bodyAsClass<Q3answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q5" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question5",
        operationId = "Question5",
        tags = ["user"],
        path = "/Q5",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q5answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q5(ctx: Context) {
        val answer = ctx.bodyAsClass<Q5answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q6" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question6",
        operationId = "Question6",
        tags = ["user"],
        path = "/Q6",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q3answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q6(ctx: Context) {
        val answer = ctx.bodyAsClass<Q3answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q7" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question7",
        operationId = "Question7",
        tags = ["user"],
        path = "/Q7",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q7answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q7(ctx: Context) {
        val answer = ctx.bodyAsClass<Q7answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * This function handles the POST request at the "/Q8" path.
     * It shows the score and ranking based on the answer provided in the request body.
     *
     * @param ctx The context of the HTTP request.
     */
    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question8",
        operationId = "Question8",
        tags = ["user"],
        path = "/Q8",
        requestBody = OpenApiRequestBody(
            required = true,
            content = [OpenApiContent(Q8answer::class)]
        ),
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Success::class)], description = "Success"),
            OpenApiResponse("500", [OpenApiContent(ErrorResponse::class)], description = "Error on Server")
        ]
    )
    fun q8(ctx: Context) {
        val answer = ctx.bodyAsClass<Q8answer>()
        logger.info { answer.answers.size }
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    @OpenApi(
        description = "Get a senario",
        deprecated = false,
        summary = "Get a senario",
        operationId = "senario",
        tags = ["user"],
        path = "/Senario/{id}",
        pathParams = [OpenApiParam("id", String::class, "The Senario ID", false, true, example = "Senario1")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(SenarioAnswer::class)]),
            OpenApiResponse("400", [OpenApiContent(ErrorResponse::class)])
        ]
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
        tags = ["user"],
        path = "/Senario/list",
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Array<String>::class)], description = "Success"),
            OpenApiResponse("400", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun listSenario(ctx: Context) {
        logger.info { "listSenario is called" }
        ctx.json(listOf("test1", "test2"))
    }

    fun createTemplateEngine(): TemplateEngine {
        val codeResolver = if (isDevSystem) {
            DirectoryCodeResolver(Path.of("src/main/jte"))
        } else {
            DirectoryCodeResolver(Path.of("jte-classes"))
        }

        return TemplateEngine.create(codeResolver, ContentType.Html)
    }
}
