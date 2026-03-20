package com.fujitsu.ikgrcscore

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response payload describing request-body validation errors.
 *
 * @property requestBody Validation issues reported for the request body.
 */
data class InvalidResponse(
    @JsonProperty("REQUEST_BODY")
    val requestBody: List<ValidationIssue>,
)

/**
 * Details for a single validation failure.
 *
 * @property message Human-readable validation error message.
 * @property args Additional validation arguments associated with the error.
 * @property value Rejected value that caused the validation failure.
 */
data class ValidationIssue(
    val message: String,
    val args: Any,
    val value: Any,
)
