package com.fujitsu.ikgrcscore

import io.javalin.json.JavalinJackson
import io.javalin.testtools.JavalinTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * This is a test class for testing the functionality of the application.
 */
class Test1 {

    // Instance of the application
    private val app = App.app

    /**
     * This test checks if the default values of the Hello page are correct.
     * It uses JavalinTest to start the server and client, and then compares the expected result with the actual result.
     *
     * @Test denotes that this function is a test case.
     * @DisplayName sets a custom name for the test case which will be displayed when the test is run.
     */
    @Test
    @DisplayName("Test Senario/list")
    fun testHelloPageDefaultValues() = JavalinTest.test(app) { server, client ->
        // Convert an array of strings to JSON
        val str = JavalinJackson().toJsonString(arrayOf("test1", "test2"), Array::class.java)
        // Assert that the response from the /Senario/list endpoint is equal to the expected JSON
        assertEquals(str, client.get("/Senario/list").body?.string())
//        assertEquals(str, client.get("/Senario/id").body?.string())
    }
}
