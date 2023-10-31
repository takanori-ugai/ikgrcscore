package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiDescription
import io.javalin.openapi.OpenApiExample

/**
 * Data class representing a HelloPage.
 *
 * @property userName The username of the user. Can be null if no username is provided.
 * @property userKarma The karma points of the user. Default value is 0.
 */
data class HelloPage(
    @JvmField
    var userName: String? = null,

    @JvmField
    var userKarma: Int = 0
) {
    @OpenApiDescription("The username of the user. Can be null if no username is provided.")
    @OpenApiExample("ugai")
    fun getUserName(): String? {
        return userName
    }

    @OpenApiDescription("The karma points of the user. Default value is 0.")
    @OpenApiExample("3")
    fun getUserKarma(): Int {
        return userKarma
    }
}
