package com.paradox.minerstats.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doAnswer
import com.paradox.minerstats.model.source.api.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.paradox.minerstats.model.dto.*
import com.paradox.minerstats.model.dto.response.ResponseDataList
import com.paradox.minerstats.model.dto.response.ResponseDataObject
import com.paradox.minerstats.model.repository.RepositoryImpl
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import java.io.IOException
import java.lang.Exception

@ExperimentalCoroutinesApi
class RepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching workers is successful should emit a worker response`() = runBlocking {
        // Given
        val workers = listOf(Worker("Name", 1644349748, 1644349748, 4.93537954, 4.83131552, 403, 0, 3))
        val workerResponse = ResponseDataList(
            "OK", workers
        )
        val apiService = mock<ApiService> {
            onBlocking { fetchWorkers("somewallet") } doReturn workerResponse
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        // When
        val flow = repository.fetchWorkers("somewallet")

        // Then
        flow.collect { result: Result<ResponseDataList<Worker>> ->
            result.isSuccess.shouldBeTrue()
            result.onSuccess { it shouldBeEqualTo workerResponse }
            result.onSuccess { it.data!!.first().worker shouldBeEqualTo "Name" }
        }
    }

    @Test
    fun `when there is an error fetching workers should emit an exception`() = runBlocking {
        // Given
        val ioException = IOException()
        val apiService = mock<ApiService> {
            onBlocking { fetchWorkers("somewallet") } doAnswer {
                throw ioException
            }
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        // When
        val flow = repository.fetchWorkers("somewallet")

        // Then
        flow.collect { result: Result<ResponseDataList<Worker>> ->
            result.isFailure.shouldBeTrue()
            result.onFailure {
                it shouldBeEqualTo ioException
            }
        }
    }

    @Test
    fun `when fetching payouts is successful should emit a payout response`() = runBlocking {
        // Given
        val payouts = listOf(
            Payout(
                "14255000",
                "14379316",
                "300065068294220200",
                "0xc47d58b935a061fe0ffa95ef740e764d90615cd46b59c7fb6e810a182f551bc8",
                1647188854
            ),
            Payout(
                "14167000",
                "14255789",
                "300013865609396200",
                "0x82aa41fae1b3d61c7406e51b04d73487a24903f5321fedb4670ea8d2edf272ee",
                1644349748
            ),
            Payout(
                "14081000",
                "14167352",
                "300023865609396200",
                "0x26f8fba2e6bc48a48a88260c49a7605b165f3fbbdc49e88ad5e37e8a8675fa94",
                1644349748
            )
        )
        val payoutResponse = ResponseDataList(
            "OK", payouts
        )
        val apiService = mock<ApiService> {
            onBlocking { fetchPayouts("somewallet") } doReturn payoutResponse
        }

        val repository = RepositoryImpl(apiService, dispatcher)

        // When
        val flow = repository.fetchPayouts("somewallet")

        // Then
        flow.collect { result: Result<ResponseDataList<Payout>> ->
            result.isSuccess.shouldBeTrue()
            result.onSuccess { it.status shouldBeEqualTo "OK" }
            result.onSuccess { it shouldBeEqualTo payoutResponse }
            result.onSuccess { it.data!!.size shouldBeEqualTo 3 }
        }
    }

    @Test
    fun `when there is an error fetching payouts should emit an exception`() = runBlocking {
        // Given
        val exception = Exception("Any Exception")
        val apiService = mock<ApiService> {
            onBlocking { fetchPayouts("somewallet") } doAnswer {
                throw exception
            }
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        // When
        val flow = repository.fetchPayouts("somewallet")

        // Then
        flow.collect { result: Result<ResponseDataList<Payout>> ->
            result.isFailure.shouldBeTrue()
            result.onFailure {
                it shouldBeEqualTo exception
                it.message shouldBeEqualTo "Any Exception"
            }
        }
    }

    @Test
    fun `when fetching statistics is successful should emit a statistic response`() = runBlocking {
        // Given
        val statistic = Stat(
            1644349748,
            1644349710,
            1.23464398848,
            1.23464300000,
            1.23464400000,
            100,
            3,
            1,
            8,
            "300023865609396200",
            2
        )

        val statisticResponse = ResponseDataObject(
            "OK", statistic
        )
        val apiService = mock<ApiService> {
            onBlocking { fetchCurrentStats("somewallet") } doReturn statisticResponse
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        // When
        val flow = repository.fetchStats("somewallet")

        // Then
        flow.collect { result: Result<ResponseDataObject<Stat>> ->
            result.isSuccess.shouldBeTrue()
            result.onSuccess { it.status shouldBeEqualTo "OK" }
            result.onSuccess { it shouldBeEqualTo statisticResponse }
        }
    }

    @Test
    fun `when there is an error fetching statistics should emit an exception`() = runBlocking {
        // Given
        val exception = Exception("Critical Exception")
        val apiService = mock<ApiService> {
            onBlocking { fetchCurrentStats("somewallet") } doAnswer {
                throw exception
            }
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        // When
        val flow = repository.fetchStats("somewallet")

        // Then
        flow.collect { result: Result<ResponseDataObject<Stat>> ->
            result.isFailure.shouldBeTrue()
            result.onFailure {
                it shouldBeEqualTo exception
                it.message shouldBeEqualTo "Critical Exception"
            }
        }
    }
}