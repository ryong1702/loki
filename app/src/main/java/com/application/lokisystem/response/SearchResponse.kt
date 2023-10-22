package com.application.lokisystem.response

import com.squareup.moshi.Json

data class SearchResponse(
    @field:Json(name = "candidates")
    val candidates: List<Candidates>?
)

data class Candidates(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "name")
    val name: String?
)