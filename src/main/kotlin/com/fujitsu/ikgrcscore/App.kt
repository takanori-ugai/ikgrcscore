package com.fujitsu.ikgrcscore

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.resolve.DirectoryCodeResolver
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
import java.util.Locale

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
        app.get("/hello") { it.html("<H1>Hello World</H1>") }
        app.get("/hello2", this::renderHelloPage)
        app.post("/helloPost", this::helloPost)
        app.get("/api/hello1") { it.result("Hello World") }
        app.get("/api/users2/{userId}", this::retHello)
        app.post("/Q1", this::q1)
        app.post("/Q2", this::q2)

        println("Check out ReDoc docs at http://localhost:$portNumber/redoc")
        println("Check out Swagger UI docs at http://localhost:$portNumber/swagger-ui")
    }

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
        println(answer.answers.size)
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    @OpenApi(
        description = "Show the score and ranking.",
        summary = "Question2",
        operationId = "Question2",
        tags = ["user"],
        path = "/Q2",
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
    fun q2(ctx: Context) {
        val answer = ctx.bodyAsClass<Q2answer>()
        println(answer.answers.size)
        ctx.json(Success(data = SuccessData(0.3, 3)))
    }

    /**
     * A test function to get a parameter from form in HTML.
     *
     * @param ctx Context the context to manipulate the query and result.
     */
    @OpenApi(
        description = "Endpoint to post a name and return it in an H1 HTML tag",
        deprecated = false,
        summary = "Post Name",
        operationId = "postName",
        tags = ["user"],
        path = "/helloPost",
        requestBody = OpenApiRequestBody(
            description = "Supports multiple request bodies",
            content = [OpenApiContent(from = String::class)]
        ),
        queryParams = [OpenApiParam("name", String::class, "The name to be posted", required = true, example = "Name")],
        methods = [HttpMethod.POST],
        responses = [
            OpenApiResponse("200", [OpenApiContent(String::class)]),
            OpenApiResponse("400", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun helloPost(ctx: Context) {
        val str = ctx.queryParams("name")
        ctx.html("<H1>$str</H1>")
    }

    @OpenApi(
        description = "Test service for Javalin",
        deprecated = false,
        summary = "Hello World",
        operationId = "hello",
        tags = ["user2"],
        path = "/users2/{userId}",
        pathParams = [OpenApiParam("userId", Int::class, "The user ID", false, true, example = "1")],
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(HelloPage::class)]),
            OpenApiResponse("400", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun retHello(ctx: Context) {
        val hello = HelloPage("Test0", 12345)
        ctx.json(hello)
    }

    @OpenApi(
        description = "Endpoint to render a HelloPage",
        deprecated = false,
        summary = "Render HelloPage",
        operationId = "renderHelloPage",
        tags = ["User"],
        path = "/hello2",
        methods = [HttpMethod.GET],
        responses = [
            OpenApiResponse("200", [OpenApiContent(HelloPage::class)]),
            OpenApiResponse("400", [OpenApiContent(ErrorResponse::class)])
        ]
    )
    fun renderHelloPage(ctx: Context) {
        val page = HelloPage("<script>alert('xss')</script>", 1337)
        ctx.render("hello.kte", mapOf("page" to page, "localizer" to Localizer(Locale.US)))
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
