package com.fujitsu.ikgrcscore

import io.javalin.openapi.OpenApiExample

/**
 * Data class representing a ranking.
 *
 * @property id The ID of the team. Example: "TeamA".
 * @property rank The rank of the team. Example: 1.
 * @property score The score of the team. Example: 20.0.
 */
data class Ranking(
    @get:OpenApiExample("TeamA")
    val id: String,
    @get:OpenApiExample("1")
    val rank: Int,
    @get:OpenApiExample("20.0")
    val score: Double,
)
