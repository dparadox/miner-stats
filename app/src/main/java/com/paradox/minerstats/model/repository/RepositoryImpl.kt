package com.paradox.minerstats.model.repository

import com.paradox.minerstats.model.dto.*
import com.paradox.minerstats.model.dto.response.ResponseDataList
import com.paradox.minerstats.model.dto.response.ResponseDataObject
import com.paradox.minerstats.model.source.api.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) : Repository {

    /**
     * Fetch Workers.
     *
     * @param miner: String wallet address.
     * @return Flow of WorkerResponse with status, the response contains a list of workers
     */

    override suspend fun fetchWorkers(miner: String): Flow<Result<ResponseDataList<Worker>>> = flow {
        val minerResponse = apiService.fetchWorkers(miner)
        emit(Result.success(minerResponse))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(defaultDispatcher)

    /**
     * Fetch Payouts.
     *
     * @param miner: String wallet address.
     * @return Flow of PayoutResponse with status, the response contains a list of payouts
     */
    override suspend fun fetchPayouts(miner: String): Flow<Result<ResponseDataList<Payout>>> = flow {
        val payoutResponse = apiService.fetchPayouts(miner)
        emit(Result.success(payoutResponse))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(defaultDispatcher)

    /**
     * Fetch the current statistic .
     *
     * @param miner: String wallet address.
     * @return Flow of StatisticResponse with status, the response contains individual metrics
     */
    override suspend fun fetchStats(miner: String): Flow<Result<ResponseDataObject<Stat>>> = flow {
        val statsResponse = apiService.fetchCurrentStats(miner)
        emit(Result.success(statsResponse))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(defaultDispatcher)
}