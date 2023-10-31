package com.fujitsu.ikgrcscore

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class HelloPageTest {

    @Test
    @DisplayName("Test HelloPage with default values")
    fun testHelloPageDefaultValues() {
        val helloPage = HelloPage()
        assertAll(
            "HelloPage",
            { assertEquals(null, helloPage.userName, "UserName should be null") },
            { assertEquals(0, helloPage.userKarma, "UserKarma should be 0") }
        )
    }

    @Test
    @DisplayName("Test HelloPage with userName only")
    fun testHelloPageWithUserNameOnly() {
        val helloPage = HelloPage(userName = "test")
        assertAll(
            "HelloPage",
            { assertEquals("test", helloPage.userName, "UserName should be 'test'") },
            { assertEquals(0, helloPage.userKarma, "UserKarma should be 0") }
        )
    }

    @Test
    @DisplayName("Test HelloPage with userKarma only")
    fun testHelloPageWithUserKarmaOnly() {
        val helloPage = HelloPage(userKarma = 1)
        assertAll(
            "HelloPage",
            { assertEquals(null, helloPage.userName, "UserName should be null") },
            { assertEquals(1, helloPage.userKarma, "UserKarma should be 1") }
        )
    }

    @Test
    @DisplayName("Test HelloPage with userName and userKarma")
    fun testHelloPageWithUserNameAndUserKarma() {
        val helloPage = HelloPage(userName = "test", userKarma = 1)
        assertAll(
            "HelloPage",
            { assertEquals("test", helloPage.userName, "UserName should be 'test'") },
            { assertEquals(1, helloPage.userKarma, "UserKarma should be 1") }
        )
    }
}
