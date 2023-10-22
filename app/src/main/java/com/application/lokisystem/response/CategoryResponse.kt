package com.application.lokisystem.response

import com.squareup.moshi.Json

data class CategoryResponse(
    @field:Json(name = "result")
    val result: String?,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "themes")
    val themes: List<Themes>?
)

data class Themes(
    @field:Json(name = "theme")
    val theme: String?,
    @field:Json(name = "data")
    val data: List<DataList>?
)

data class DataList(
    @field:Json(name = "company")
    val company: String?,
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "colorCode")
    val colorCode: String?,
    @field:Json(name = "supply")
    val supply: Int?,
    @field:Json(name = "initial")
    val initial: String,
    @field:Json(name = "imgSrc")
    val imgSrc: String?,
    @field:Json(name = "tradingHalt")
    val tradingHalt: Int?
)