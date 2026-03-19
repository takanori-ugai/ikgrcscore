package com.fujitsu.ikgrcscore

/**
 * Data class representing a collection of ranking entries.
 *
 * @property rankings The ordered ranking entries returned by the API.
 */
data class RankingList(val rankings: List<Ranking>)
