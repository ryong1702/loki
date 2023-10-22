package com.application.lokisystem.response

import com.squareup.moshi.Json

data class MarketResponse(
    @field:Json(name = "result")
    val result: String?,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "market")
    val market: String,
    @field:Json(name = "closePrice")
    val closePrice: String?,
    @field:Json(name = "beforeRatio")
    val beforeRatio: String?,
    @field:Json(name = "arrow")
    val arrow: Int?,
    @field:Json(name = "marketStatus")
    val marketStatus: String?,
    @field:Json(name = "standardDate")
    val standardDate: String
)