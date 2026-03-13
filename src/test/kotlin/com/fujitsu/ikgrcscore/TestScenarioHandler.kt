package com.fujitsu.ikgrcscore

import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.json.JavalinJackson
import io.javalin.json.toJsonString
import io.javalin.testtools.JavalinTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Test suite for ScenarioHandler endpoints introduced/renamed in the PR.
 * Tests the renamed methods: getScenario and listScenario.
 */
class TestScenarioHandler {
    private val logger = KotlinLogging.logger {}

    /**
     * Test the /Scenario/list endpoint returns the expected list.
     */
    @Test
    @DisplayName("Test Scenario/list endpoint")
    fun testScenarioList() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val expected = JavalinJackson().toJsonString(listOf("test1", "test2"))
            val response = client.get("/Scenario/list")
            assertEquals(200, response.code)
            assertEquals(expected, response.body.string())
        }

    /**
     * Test the /Scenario/{id} endpoint with a valid ID.
     */
    @Test
    @DisplayName("Test Scenario/{id} with valid ID")
    fun testGetScenarioValidId() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val expected =
                JavalinJackson().toJsonString(
                    ScenarioAnswer(data = Scenario("Scenario1", "Scenario1", 1, listOf("Test"))),
                )
            val response = client.get("/Scenario/validId")
            assertEquals(200, response.code)
            assertEquals(expected, response.body.string())
        }

    /**
     * Test the /Scenario/{id} endpoint with a different ID.
     * Since the implementation always returns the same data, verify it still works.
     */
    @Test
    @DisplayName("Test Scenario/{id} with different ID")
    fun testGetScenarioDifferentId() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val expected =
                JavalinJackson().toJsonString(
                    ScenarioAnswer(data = Scenario("Scenario1", "Scenario1", 1, listOf("Test"))),
                )
            val response = client.get("/Scenario/Scenario2")
            assertEquals(200, response.code)
            assertEquals(expected, response.body.string())
        }

    /**
     * Test the /Scenario/{id} endpoint with a numeric ID.
     */
    @Test
    @DisplayName("Test Scenario/{id} with numeric ID")
    fun testGetScenarioNumericId() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val expected =
                JavalinJackson().toJsonString(
                    ScenarioAnswer(data = Scenario("Scenario1", "Scenario1", 1, listOf("Test"))),
                )
            val response = client.get("/Scenario/123")
            assertEquals(200, response.code)
            assertEquals(expected, response.body.string())
        }

    /**
     * Test the /Scenario/{id} endpoint with special characters in ID.
     */
    @Test
    @DisplayName("Test Scenario/{id} with special characters")
    fun testGetScenarioSpecialCharsId() =
        JavalinTest.test(App().javalinApp) { _, client ->
            val expected =
                JavalinJackson().toJsonString(
                    ScenarioAnswer(data = Scenario("Scenario1", "Scenario1", 1, listOf("Test"))),
                )
            val response = client.get("/Scenario/test-scenario_1")
            assertEquals(200, response.code)
            assertEquals(expected, response.body.string())
        }

    /**
     * Test that ScenarioAnswer data class has correct default values.
     */
    @Test
    @DisplayName("Test ScenarioAnswer default values")
    fun testScenarioAnswerDefaults() {
        val scenario = Scenario("id1", "title1", 1, listOf("activity1"))
        val scenarioAnswer = ScenarioAnswer(data = scenario)

        assertEquals(200, scenarioAnswer.statusCode)
        assertEquals("GET", scenarioAnswer.method)
        assertEquals("Succeed", scenarioAnswer.message)
        assertEquals(scenario, scenarioAnswer.data)
    }

    /**
     * Test that Scenario data class can be created with various values.
     */
    @Test
    @DisplayName("Test Scenario data class creation")
    fun testScenarioCreation() {
        val scenario = Scenario("Scenario1", "Test Title", 5, listOf("Act1", "Act2", "Act3"))

        assertEquals("Scenario1", scenario.id)
        assertEquals("Test Title", scenario.title)
        assertEquals(5, scenario.scene)
        assertEquals(listOf("Act1", "Act2", "Act3"), scenario.activities)
    }

    /**
     * Test ScenarioAnswer with custom values instead of defaults.
     */
    @Test
    @DisplayName("Test ScenarioAnswer with custom values")
    fun testScenarioAnswerCustomValues() {
        val scenario = Scenario("CustomId", "Custom Title", 2, listOf("CustomActivity"))
        val scenarioAnswer = ScenarioAnswer(
            statusCode = 201,
            method = "POST",
            message = "Created",
            data = scenario,
        )

        assertEquals(201, scenarioAnswer.statusCode)
        assertEquals("POST", scenarioAnswer.method)
        assertEquals("Created", scenarioAnswer.message)
        assertEquals("CustomId", scenarioAnswer.data.id)
    }

    /**
     * Test that empty activities list is handled correctly.
     */
    @Test
    @DisplayName("Test Scenario with empty activities")
    fun testScenarioEmptyActivities() {
        val scenario = Scenario("EmptyId", "Empty Activities", 0, emptyList())

        assertEquals(emptyList<String>(), scenario.activities)
        assertEquals(0, scenario.scene)
    }
}