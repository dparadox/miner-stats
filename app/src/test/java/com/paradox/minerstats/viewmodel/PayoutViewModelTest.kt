package com.paradox.minerstats.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Test
import org.junit.Before
import com.nhaarman.mockitokotlin2.mock
import com.paradox.minerstats.LiveDataTestObserver
import com.paradox.minerstats.R
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.adapter.ResourceAdapter
import com.paradox.minerstats.core.config.PrefKeys
import com.paradox.minerstats.model.dto.*
import com.paradox.minerstats.model.dto.response.ResponseDataList
import com.paradox.minerstats.model.repository.RepositoryImpl
import com.paradox.minerstats.model.source.api.network.ApiService
import com.paradox.minerstats.ui.payout.PayoutViewModel
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation.ShowError
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation.ShowPayoutDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.kotlin.doAnswer
import java.lang.Exception

@ExperimentalCoroutinesApi
class PayoutViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = TestCoroutineDispatcher()

    private val resourceAdapter = mock<ResourceAdapter>()
    private lateinit var keyValueDataStore: KeyValueDataStore
    private lateinit var viewModel: PayoutViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        keyValueDataStore = mock {
            on { getString(PrefKeys.KEY_WALLET_ADDRESS) }.doReturn("B3C196D1B394c688c889dE96d1bFD58a6D08371c")
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when fetching payouts will return a success response`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<List<Payout>> = LiveDataTestObserver()
        val payoutsList = listOf(
            Payout(
                "14255000",
                "14379316",
                "300065068294220200",
                "0xc47d58b935a061fe0ffa95ef740e764d90615cd46b59c7fb6e810a182f551bc8",
                1647188854
            )
        )
        val payoutResponse = ResponseDataList(
            "OK", payoutsList
        )
        val apiService = mock<ApiService> {
            onBlocking { fetchPayouts("B3C196D1B394c688c889dE96d1bFD58a6D08371c") } doReturn payoutResponse
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        viewModel = PayoutViewModel(repository, resourceAdapter, keyValueDataStore).apply {
            payouts.observeForever(testObserver)
        }

        launch {
            // When
            viewModel.fetchPayouts()

            // Then
            testObserver.assertSingleEmission(payoutsList)
            testObserver.assertTotalEmissions(1)
        }
    }

    @Test
    fun `when fetching payouts fails will emit an error event`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<List<Payout>> = LiveDataTestObserver()
        val singleLiveEventObserver: LiveDataTestObserver<PayoutNavigation> = LiveDataTestObserver()

        val errorMessage = "Some general error"
        val exception = Exception("IO Exception")
        val apiService = mock<ApiService> {
            onBlocking { fetchPayouts("B3C196D1B394c688c889dE96d1bFD58a6D08371c") } doAnswer {
                throw exception
            }
        }

        val localResourceAdapter: ResourceAdapter = mock {
            on { getString(R.string.general_error) }.doReturn(errorMessage)
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        viewModel = PayoutViewModel(repository, localResourceAdapter, keyValueDataStore).apply {
            payouts.observeForever(testObserver)
            navigationEvents.observeForever(singleLiveEventObserver)
        }

        launch {
            // When
            viewModel.fetchPayouts()

            // Then
            testObserver.assertTotalEmissions(0)
            singleLiveEventObserver.assertTotalEmissions(1)
            viewModel.navigationEvents.value shouldBeEqualTo ShowError(errorMessage)
        }
    }

    @Test
    fun `when tapping onExploreTapped should emit an event`() = runTest {
        // When
        val testObserver: LiveDataTestObserver<PayoutNavigation> = LiveDataTestObserver()
        val uri = "https://etherscan.io/tx/0x000000000"
        val resourceAdapter = mock<ResourceAdapter> {
            on { getString(R.string.ether_scan_tx_uri, "0x000000000") } doReturn (uri)
        }
        viewModel = PayoutViewModel(mock(), resourceAdapter, keyValueDataStore).apply {
            navigationEvents.observeForever(testObserver)
        }

        // When
        viewModel.onExploreTapped("0x000000000")

        // Then
        viewModel.navigationEvents.value shouldBeEqualTo ShowPayoutDetail(uri)
    }
}