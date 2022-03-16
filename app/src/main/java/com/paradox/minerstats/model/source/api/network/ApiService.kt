package com.paradox.minerstats.model.source.api.network

import com.paradox.minerstats.model.dto.*
import com.paradox.minerstats.model.dto.response.ResponseDataList
import com.paradox.minerstats.model.dto.response.ResponseDataObject
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("miner/{wallet}/workers")
    suspend fun fetchWorkers(@Path(value="wallet", encoded = true) wallet: String): ResponseDataList<Worker>

    @GET("miner/{wallet}/payouts")
    suspend fun fetchPayouts(@Path(value="wallet", encoded = true) wallet: String): ResponseDataList<Payout>

    @GET("miner/{wallet}/currentStats")
    suspend fun fetchCurrentStats(@Path(value="wallet", encoded = true) wallet: String): ResponseDataObject<Stat>
}