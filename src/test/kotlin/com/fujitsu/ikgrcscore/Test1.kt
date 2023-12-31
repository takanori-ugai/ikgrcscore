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
class Test1 {
    private val logger = KotlinLogging.logger {}

    /**
     * Validates the default values of the Hello page by comparing the actual result
     * with the expected result.
     */
    @Test
    @DisplayName("Test Senario/list")
    fun testHelloPageDefaultValues() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(listOf("test1", "test2"))
            // Assert that the response from the /Senario/list endpoint is equal to the expected JSON
            assertEquals(str, client.get("/Senario/list").body?.string())
//        assertEquals(str, client.get("/Senario/id").body?.string())
        }

    /**
     * This test checks if the response from the /Senario/id endpoint is correct.
     * It uses JavalinTest to start the server and client, and then compares the expected result
     * with the actual result.
     */
    @Test
    @DisplayName("Test Senario/id")
    fun testSenarioId() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str =
                JavalinJackson().toJsonString(
                    SenarioAnswer(data = Senario("Senario1", "Senario1", 1, listOf("Test"))),
                )
            // Assert that the response from the /Senario/list endpoint is equal to the expected JSON
            assertEquals(str, client.get("/Senario/id").body?.string())
//        assertEquals(str, client.get("/Senario/id").body?.string())
        }

    /**
     * Validates the response from the /Senario/id endpoint by comparing the actual result
     * with the expected result.
     */
    @Test
    @DisplayName("Test Q1 Not empty")
    fun testQ1() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Senario/list endpoint is equal to the expected JSON
            assertEquals(str, client.post("/Q1", Q1answer("A", "B", listOf(Q1data("C", 1)))).body?.string())
        }

    /**
     * Validates the response from the /Q1 endpoint when the request body is not empty
     * by comparing the actual result with the expected result.
     */
    @Test
    @DisplayName("Test Q1 Empty")
    fun testQ1empty() =
        JavalinTest.test(App().app) { _, client ->
            val res = client.post("/Q1", Q1answer("", "", emptyList()))
            logger.info { res.code }
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            logger.info { resStr }
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    /**
     * Validates the response from the /Q1 endpoint when the request body is empty
     * by comparing the actual result with the expected result.
     */
    @Test
    @DisplayName("Test Q2 Not empty")
    fun testQ2() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q2 endpoint is equal to the expected JSON
            // Replace Q1answer and Q1data with the appropriate classes for Q2
            assertEquals(str, client.post("/Q2", Q2answer("A", "B", listOf(Q2data("NAME", 2)))).body?.string())
        }

    /**
     * Validates the response from the /Q2 endpoint when the request body is not empty
     * by comparing the actual result with the expected result.
     */
    @Test
    @DisplayName("Test Q2 Empty")
    fun testQ2empty() =
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q2 endpoint
            // Replace Q1answer with the appropriate class for Q2
            val res = client.post("/Q2", Q2answer("", "", emptyList()))
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    @Test
    @DisplayName("Test Q3 Not empty")
    fun testQ3() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q3 endpoint is equal to the expected JSON
            assertEquals(str, client.post("/Q3", Q3answer("A", "B", listOf("C"))).body?.string())
        }

    @Test
    @DisplayName("Test Q3 Empty")
    fun testQ3empty() =
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q3 endpoint
            val res = client.post("/Q3", Q3answer("", "", emptyList()))
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    /**
     * This test validates the response from the /Q4 endpoint when the request body is not empty.
     * It posts a Q3answer object to the /Q4 endpoint and checks if the response status code and
     * body match the expected values.
     */
    @Test
    @DisplayName("Test Q4 Not empty")
    fun testQ4() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q4 endpoint is equal to the expected JSON
            assertEquals(str, client.post("/Q4", Q3answer("A", "B", listOf("C"))).body?.string())
        }

    /**
     * This test validates the response from the /Q4 endpoint when the request body is empty.
     * It posts an empty Q3answer object to the /Q4 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q4 Empty")
    fun testQ4empty() =
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q4 endpoint
            val res = client.post("/Q4", Q3answer("", "", emptyList()))
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    /**
     * This test validates the response from the /Q5 endpoint when the request body is not empty.
     * It posts a Q5answer object to the /Q5 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q5 Not empty")
    fun testQ5() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q5 endpoint is equal to the expected JSON
            assertEquals(
                str,
                client.post(
                    "/Q5",
                    Q5answer(
                        "A",
                        "B",
                        listOf(Q5data("2022-01-01T00:00:20.005", "LivingRoom", listOf("Cup"))),
                    ),
                ).body?.string(),
            )
        }

    /**
     * This test validates the response from the /Q5 endpoint when the request body is empty.
     * It posts an empty Q5answer object to the /Q5 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q5 Empty")
    fun testQ5empty() =
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q5 endpoint
            val res = client.post("/Q5", Q5answer("", "", emptyList()))
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            val resObj =
                JavalinJackson(
                    objectMapper =
                        ObjectMapper()
                            .registerKotlinModule(),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    /**
     * This test validates the response from the /Q6 endpoint when the request body is not empty.
     * It posts a Q6answer object to the /Q6 endpoint and checks if the response status code and
     * body match the expected values.
     */
    @Test
    @DisplayName("Test Q6 Not empty")
    fun testQ6() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q6 endpoint is equal to the expected JSON
            assertEquals(str, client.post("/Q6", Q6answer("Takanori Ugai", "Senario1", "Grab")).body?.string())
        }

    /**
     * This test validates the response from the /Q6 endpoint when the request body is empty.
     * It posts an empty Q6answer object to the /Q6 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q6 Empty")
    fun testQ6empty() =
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q6 endpoint
            val res = client.post("/Q6", Q6answer("", "", ""))
            assertEquals(400, res.code)
            val resStr = res.body?.string()
            val resObj =
                JavalinJackson(
                    objectMapper = ObjectMapper().registerKotlinModule(),
                )
                    .fromJsonString<InvalidResponse>(resStr!!)
            assertEquals("Name must not be empty", resObj.requestBody[0].message)
            assertEquals("Senario must not be empty", resObj.requestBody[1].message)
            assertEquals("Answers must not be empty", resObj.requestBody[2].message)
        }

    /**
     * This test validates the response from the /Q7 endpoint when the request body is not empty.
     * It posts a Q7answer object to the /Q7 endpoint and checks if the response status code
     * and body match the expected values.
     */
    @Test
    @DisplayName("Test Q7 Not empty")
    fun testQ7() =
        JavalinTest.test(App().app) { _, client ->
            // Convert an array of strings to JSON
            val str = JavalinJackson().toJsonString(Success(data = SuccessData(0.3, 3)))
            // Assert that the response from the /Q7 endpoint is equal to the expected JSON
            assertEquals(
                str,
                client.post("/Q7", Q7answer("Takanori Ugai", "Senario1", listOf(Q7data("Table", "Cup", "ON")))).body?.string(),
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
        JavalinTest.test(App().app) { _, client ->
            // Post an empty answer to the /Q7 endpoint
            val res = client.post("/Q7", Q7answer("", "", emptyList()))
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
