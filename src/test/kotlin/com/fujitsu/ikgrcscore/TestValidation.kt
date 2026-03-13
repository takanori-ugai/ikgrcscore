package com.fujitsu.ikgrcscore

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.json.JavalinJackson
import io.javalin.json.fromJsonString
import io.javalin.testtools.JavalinTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Test suite for validating the new nullable validation logic introduced in the PR.
 * Tests edge cases for the updated validation: it?.field.isNullOrBlank() == false
 */
class TestValidation {
    private val logger = KotlinLogging.logger {}

    /**
     * Test Q1 endpoint with whitespace-only name field.
     * The new validation logic should reject whitespace-only strings.
     */
    @Test
    @DisplayName("Test Q1 with whitespace-only name")
    fun testQ1WhitespaceName() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q1", Q1answer("   ", "Scenario1", setOf(Q1data("C", 1))))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
        }

    /**
     * Test Q1 endpoint with whitespace-only scenario field.
     */
    @Test
    @DisplayName("Test Q1 with whitespace-only scenario")
    fun testQ1WhitespaceScenario() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q1", Q1answer("Takanori Ugai", "   ", setOf(Q1data("C", 1))))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Scenario must not be empty", resObj.requestBody[0].message)
        }

    /**
     * Test Q2 endpoint with whitespace-only fields.
     */
    @Test
    @DisplayName("Test Q2 with whitespace-only name and scenario")
    fun testQ2WhitespaceFields() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q2", Q2answer("  ", "  ", setOf(Q2data("NAME", 2))))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Scenario must not be empty", resObj.requestBody[1].message)
        }

    /**
     * Test Q3 endpoint with tab characters in name field.
     */
    @Test
    @DisplayName("Test Q3 with tab character name")
    fun testQ3TabName() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q3", Q3answer("\t", "Scenario1", setOf("C")))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
        }

    /**
     * Test Q5 endpoint with newline characters in scenario field.
     */
    @Test
    @DisplayName("Test Q5 with newline scenario")
    fun testQ5NewlineScenario() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res =
                client.post(
                    "/Q5",
                    Q5answer(
                        "Takanori Ugai",
                        "\n",
                        setOf(Q5data("2022-01-01T00:00:20.005", "LivingRoom", setOf("Cup"))),
                    ),
                )
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Scenario must not be empty", resObj.requestBody[0].message)
        }

    /**
     * Test Q6 endpoint with whitespace-only answers field (String type).
     */
    @Test
    @DisplayName("Test Q6 with whitespace-only answers")
    fun testQ6WhitespaceAnswers() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q6", Q6answer("Takanori Ugai", "Scenario1", "   "))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Answers must not be empty", resObj.requestBody[0].message)
        }

    /**
     * Test Q7 endpoint with all fields containing only whitespace.
     */
    @Test
    @DisplayName("Test Q7 with all whitespace fields")
    fun testQ7AllWhitespace() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q7", Q7answer(" ", " ", emptySet()))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Scenario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    /**
     * Test Q8 endpoint with mixed whitespace characters.
     */
    @Test
    @DisplayName("Test Q8 with mixed whitespace")
    fun testQ8MixedWhitespace() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q8", Q8answer(" \t\n ", "Scenario1", emptySet()))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
        }

    /**
     * Test Q4 endpoint with only scenario field being whitespace.
     * This tests that individual field validation works correctly.
     */
    @Test
    @DisplayName("Test Q4 with valid name but whitespace scenario")
    fun testQ4PartialWhitespace() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val res = client.post("/Q4", Q3answer("Valid Name", "  \t  ", setOf("C")))
            assertEquals(400, res.code)
            val resStr = res.body.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                ).fromJsonString<InvalidResponse>(resStr)
            assertEquals("Scenario must not be empty", resObj.requestBody[0].message)
        }
}
