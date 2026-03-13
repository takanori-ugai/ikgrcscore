package com.fujitsu.ikgrcscore

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Test suite for the Answer generic data class.
 * Tests the renamed 'scenario' field (previously 'senario') and generic type parameter functionality.
 */
class TestAnswer {
    /**
     * Test Answer data class with Set<String> as the generic type.
     */
    @Test
    @DisplayName("Test Answer with Set<String> type")
    fun testAnswerWithSetString() {
        val answer =
            Answer(
                name = "Takanori Ugai",
                scenario = "Scenario1",
                answers = setOf("WALK", "GRAB"),
            )

        assertEquals("Takanori Ugai", answer.name)
        assertEquals("Scenario1", answer.scenario)
        assertEquals(setOf("WALK", "GRAB"), answer.answers)
    }

    /**
     * Test Answer data class with Set<Q1data> as the generic type.
     */
    @Test
    @DisplayName("Test Answer with Set<Q1data> type")
    fun testAnswerWithSetQ1data() {
        val q1Data = setOf(Q1data("Kitchen", 1), Q1data("Bathroom", 2))
        val answer =
            Answer(
                name = "Test User",
                scenario = "Scenario2",
                answers = q1Data,
            )

        assertEquals("Test User", answer.name)
        assertEquals("Scenario2", answer.scenario)
        assertEquals(q1Data, answer.answers)
    }

    /**
     * Test Answer data class with Set<Q2data> as the generic type.
     */
    @Test
    @DisplayName("Test Answer with Set<Q2data> type")
    fun testAnswerWithSetQ2data() {
        val q2Data = setOf(Q2data("ACTION", 3))
        val answer =
            Answer(
                name = "Jane Doe",
                scenario = "Scenario3",
                answers = q2Data,
            )

        assertEquals("Jane Doe", answer.name)
        assertEquals("Scenario3", answer.scenario)
        assertEquals(q2Data, answer.answers)
    }

    /**
     * Test Answer data class with String as the generic type (for Q6).
     */
    @Test
    @DisplayName("Test Answer with String type")
    fun testAnswerWithString() {
        val answer =
            Answer(
                name = "John Smith",
                scenario = "Scenario4",
                answers = "Grab",
            )

        assertEquals("John Smith", answer.name)
        assertEquals("Scenario4", answer.scenario)
        assertEquals("Grab", answer.answers)
    }

    /**
     * Test Answer data class with Set<Q5data> as the generic type.
     */
    @Test
    @DisplayName("Test Answer with Set<Q5data> type")
    fun testAnswerWithSetQ5data() {
        val q5Data =
            setOf(
                Q5data("2022-01-01T00:00:20.005", "LivingRoom", setOf("Cup", "Book")),
                Q5data("2022-01-01T00:00:30.010", "Kitchen", setOf("Plate")),
            )
        val answer =
            Answer(
                name = "Test Name",
                scenario = "Scenario5",
                answers = q5Data,
            )

        assertEquals("Test Name", answer.name)
        assertEquals("Scenario5", answer.scenario)
        assertEquals(q5Data, answer.answers)
    }

    /**
     * Test Answer data class with Set<Q7data> as the generic type.
     */
    @Test
    @DisplayName("Test Answer with Set<Q7data> type")
    fun testAnswerWithSetQ7data() {
        val q7Data =
            setOf(
                Q7data("Table", "Cup", "ON"),
                Q7data("Floor", "Book", "BESIDE"),
            )
        val answer =
            Answer(
                name = "Alice",
                scenario = "Scenario6",
                answers = q7Data,
            )

        assertEquals("Alice", answer.name)
        assertEquals("Scenario6", answer.scenario)
        assertEquals(q7Data, answer.answers)
    }

    /**
     * Test Answer data class with Set<Q8data> as the generic type.
     */
    @Test
    @DisplayName("Test Answer with Set<Q8data> type")
    fun testAnswerWithSetQ8data() {
        val q8Data =
            setOf(
                Q8data("Table", setOf(Q8element(listOf(1.1, 2.5, 3.2), setOf("ON", "CLEAN")))),
            )
        val answer =
            Answer(
                name = "Bob",
                scenario = "Scenario7",
                answers = q8Data,
            )

        assertEquals("Bob", answer.name)
        assertEquals("Scenario7", answer.scenario)
        assertEquals(q8Data, answer.answers)
    }

    /**
     * Test that Answer data class with empty set is valid.
     */
    @Test
    @DisplayName("Test Answer with empty set")
    fun testAnswerWithEmptySet() {
        val answer =
            Answer(
                name = "User",
                scenario = "ScenarioEmpty",
                answers = emptySet<String>(),
            )

        assertEquals("User", answer.name)
        assertEquals("ScenarioEmpty", answer.scenario)
        assertEquals(emptySet<String>(), answer.answers)
    }

    /**
     * Test Answer equality - same values should be equal (data class behavior).
     */
    @Test
    @DisplayName("Test Answer equality")
    fun testAnswerEquality() {
        val answer1 =
            Answer(
                name = "Test",
                scenario = "Scenario1",
                answers = setOf("A", "B"),
            )
        val answer2 =
            Answer(
                name = "Test",
                scenario = "Scenario1",
                answers = setOf("A", "B"),
            )

        assertEquals(answer1, answer2)
        assertEquals(answer1.hashCode(), answer2.hashCode())
    }

    /**
     * Test Answer inequality - different values should not be equal.
     */
    @Test
    @DisplayName("Test Answer inequality")
    fun testAnswerInequality() {
        val answer1 =
            Answer(
                name = "Test1",
                scenario = "Scenario1",
                answers = setOf("A"),
            )
        val answer2 =
            Answer(
                name = "Test2",
                scenario = "Scenario1",
                answers = setOf("A"),
            )

        assertNotEquals(answer1, answer2)
    }

    /**
     * Test Answer copy functionality (data class feature).
     */
    @Test
    @DisplayName("Test Answer copy functionality")
    fun testAnswerCopy() {
        val original =
            Answer(
                name = "Original",
                scenario = "Scenario1",
                answers = setOf("X"),
            )
        val copied = original.copy(name = "Modified")

        assertEquals("Modified", copied.name)
        assertEquals("Scenario1", copied.scenario)
        assertEquals(setOf("X"), copied.answers)
        assertEquals("Original", original.name)
    }

    /**
     * Test that scenario field (renamed from senario) is properly accessible.
     */
    @Test
    @DisplayName("Test scenario field name")
    fun testScenarioFieldName() {
        val answer =
            Answer(
                name = "User",
                scenario = "TestScenario",
                answers = listOf("item1", "item2"),
            )

        // Verify the field is named 'scenario', not 'senario'
        assertEquals("TestScenario", answer.scenario)
    }
}
