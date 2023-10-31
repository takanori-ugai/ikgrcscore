package com.fujitsu.ikgrcscore

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.patch
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.openapi.OpenApiContact
import io.javalin.openapi.OpenApiLicense
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.OpenApiPluginConfiguration
import io.javalin.openapi.plugin.redoc.ReDocConfiguration
import io.javalin.openapi.plugin.redoc.ReDocPlugin
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration
import io.javalin.openapi.plugin.swagger.SwaggerPlugin

/**
 * The port number on which the server will be started.
 */
const val PORT_NUMBER = 7001

/**
 * This is the main function which is the entry point of any Kotlin program.
 * It sets up a Javalin server with OpenAPI, Swagger and ReDoc plugins,
 * and defines routes for user operations.
 */
fun main() {
    val app = Javalin.create { config ->
        val deprecatedDocsPath = "/swagger-docs"
        config.plugins.register(
            OpenApiPlugin(
                OpenApiPluginConfiguration()
                    .withDocumentationPath(deprecatedDocsPath)
                    .withDefinitionConfiguration { _, definition ->
                        definition
                            .withOpenApiInfo { openApiInfo ->
                                val openApiContact = OpenApiContact()
                                openApiContact.name = "API Support"
                                openApiContact.url = "https://www.example.com/support"
                                openApiContact.email = "ugai@fujitsu.com"
                                val openApiLicense = OpenApiLicense()
                                openApiLicense.name = "Apache 2.0"
                                openApiLicense.identifier = "Apache-2.0"
                                openApiInfo.title = "Test Application"
                                openApiInfo.summary = "App summary"
                                openApiInfo.description = "Test Description"
                                openApiInfo.termsOfService = "https://example.com/tos"
                                openApiInfo.contact = openApiContact
                                openApiInfo.license = openApiLicense
                                openApiInfo.version = "1.0.0"
                            }
                    }
            )
        )
        val swaggerConfiguration = SwaggerConfiguration()
        swaggerConfiguration.uiPath = "/swagger-ui" // by default it's /swagger
        swaggerConfiguration.documentationPath = deprecatedDocsPath
        config.plugins.register(SwaggerPlugin(swaggerConfiguration))
        val reDocConfiguration = ReDocConfiguration()
        reDocConfiguration.uiPath = "/redoc" // by default it's /redoc
        reDocConfiguration.documentationPath = deprecatedDocsPath
        config.plugins.register(ReDocPlugin(reDocConfiguration))
    }.routes {
        path("users") {
            get(UserController::getAll)
//            post(documented(createUserDocumentation, UserController::create))
            path("{userId}") {
                get(UserController::getOne)
                patch(UserController::update)
                delete(UserController::delete)
            }
        }
    }.start(PORT_NUMBER)
    app.get("/user2", UserController::getAll)

    println("Check out ReDoc docs at http://localhost:7001/redoc")
    println("Check out Swagger UI docs at http://localhost:7001/swagger-ui")
}

/**
 * This is a test class with no functionality.
 */
class Test2
