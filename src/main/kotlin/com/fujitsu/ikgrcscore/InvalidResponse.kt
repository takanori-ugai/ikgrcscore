package com.fujitsu.ikgrcscore

import com.fasterxml.jackson.annotation.JsonProperty

data class InvalidResponse(
    @JsonProperty("REQUEST_BODY")
    val requestBody: List<Res>
)
data class Res(
    val message: String,
    val args: Any,
    val value: Any
)
