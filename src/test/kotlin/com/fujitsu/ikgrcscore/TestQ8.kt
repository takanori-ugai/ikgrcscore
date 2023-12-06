package com.fujitsu.ikgrcscore

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.json.JavalinJackson
import io.javalin.json.fromJsonString
import io.javalin.json.toJsonString
import io.javalin.testtools.JavalinTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Test suite for validating application functionality.
 */
class TestQ8 {
    private val logger = KotlinLogging.logger {}

    /**
     * This test validates the response from the /Q8 endpoint when the request body is not empty.
     * It posts a Q8answer object to the /Q8 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q8 Not empty")
    fun testQ8() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q8 endpoint is equal to the expected JSON
            assertEquals(
                str,
                client.post(
                    "/Q8",
                    Q8answer(
                        "Takanori Ugai",
                        "Senario1",
                        listOf(Q8data("Table", listOf(Q8element(listOf(1.1, 2.5, 3.2), listOf("ON", "CLEAN"))))),
                    ),
                ).body?.string(),
            )
        }

    /**
     * This test validates the response from the /Q8 endpoint when the request body is empty.
     * It posts an empty Q8answer object to the /Q8 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q8 Empty")
    fun testQ8empty() =
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q8 endpoint
            val res = client.post("/Q8", Q8answer("", "", emptyList()))
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            val resObj =
                JavalinJackson(
                    objectMapper =
                        ObjectMapper()
                            .registerKotlinModule()
                            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }
}
