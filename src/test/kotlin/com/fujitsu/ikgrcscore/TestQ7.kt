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
class TestQ7 {
    private val logger = KotlinLogging.logger {}

    /**
     * This test validates the response from the /Q7 endpoint when the request body is not empty.
     * It posts a Q7answer object to the /Q7 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q7 Not empty")
    fun testQ7NotEmpty() =
        JavalinTest.test(App().javalinApp) { _, client ->
            // Convert an array of strings to JSON
            val str =
                JavalinJackson().toJsonString(
                    Success(data = SuccessData(App.DEFAULT_SCORE, App.DEFAULT_RANK)),
                )
            val answer = Q7answer("Takanori Ugai", "Senario1", setOf(Q7data("Table", "Cup", "ON")))
            // Assert that the response from the /Q7 endpoint is equal to the expected JSON
            assertEquals(
                str,
                client.post("/Q7", answer).body?.string(),
            )
        }

    /**
     * This test validates the response from the /Q7 endpoint when the request body is empty.
     * It posts an empty Q7answer object to the /Q7 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q7 Empty")
    fun testQ7empty() =
        JavalinTest.test(App().javalinApp) { _, client ->
            // Post an empty answer to the /Q7 endpoint
            val res = client.post("/Q7", Q7answer("", "", emptySet()))
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
