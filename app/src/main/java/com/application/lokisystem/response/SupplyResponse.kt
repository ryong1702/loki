package com.application.lokisystem.response

import com.squareup.moshi.Json

data class SupplyResponse(
    @field:Json(name = "result")
    val result: String?,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "top5")
    val top5: List<Top5>?
)

data class Top5(
    @field:Json(name = "company")
    val company: String?,
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "colorCode")
    val colorCode: String?,
    @field:Json(name = "supply")
    val supply: Int?,
    @field:Json(name = "initial")
    val initial: String?,
    @field:Json(name = "imgSrc")
    val imgSrc: String?,
    @field:Json(name = "tradingHalt")
    val tradingHalt: Int?
)
