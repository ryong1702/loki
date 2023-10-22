package com.application.lokisystem.service

import com.application.lokisystem.response.MarketResponse
import com.application.lokisystem.response.SupplyResponse
import com.application.lokisystem.response.CategoryResponse
import com.application.lokisystem.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/api/v2/m/top5")
    suspend fun getSupplyData(): Response<SupplyResponse>

    @GET("/api/v1/m/market-status")
    suspend fun getMarketStatusData(): Response<MarketResponse>

    @GET("/api/v2/m/theme")
    suspend fun getThemeData(): Response<CategoryResponse>

    @GET("/api/v1/m/search/candidate")
    suspend fun getCandidateData(): Response<SearchResponse>

}