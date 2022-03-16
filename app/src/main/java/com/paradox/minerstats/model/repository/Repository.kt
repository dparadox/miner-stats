package com.paradox.minerstats.model.repository

import com.paradox.minerstats.model.dto.*
import com.paradox.minerstats.model.dto.response.ResponseDataList
import com.paradox.minerstats.model.dto.response.ResponseDataObject
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun fetchWorkers(miner: String) : Flow<Result<ResponseDataList<Worker>>>

    suspend fun fetchPayouts(miner: String) : Flow<Result<ResponseDataList<Payout>>>

    suspend fun fetchStats(miner: String) : Flow<Result<ResponseDataObject<Stat>>>
}