package com.paradox.minerstats.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.paradox.minerstats.model.dto.Worker
import com.paradox.minerstats.ui.home.HomeViewModel
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
import com.paradox.minerstats.model.dto.response.ResponseDataList
import com.paradox.minerstats.model.dto.response.ResponseDataObject
import com.paradox.minerstats.model.dto.Stat
import com.paradox.minerstats.model.repository.RepositoryImpl
import com.paradox.minerstats.model.source.api.network.ApiService
import com.paradox.minerstats.ui.home.navigation.HomeNavigation
import com.paradox.minerstats.ui.home.navigation.HomeNavigation.ShowError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.kotlin.doAnswer
import java.lang.Exception

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = TestCoroutineDispatcher()

    private val resourceAdapter = mock<ResourceAdapter>()
    private lateinit var keyValueDataStore: KeyValueDataStore
    private lateinit var viewModel: HomeViewModel

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
    fun `when fetching workers will return a success response`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<List<Worker>> = LiveDataTestObserver()
        val workerList = listOf(Worker("Name", 1644349748, 1644349748, 4.93537954, 4.83131552, 403, 0, 3))
        val workerResponse = ResponseDataList(
            "OK", workerList
        )
        val apiService = mock<ApiService> {
            onBlocking { fetchWorkers("B3C196D1B394c688c889dE96d1bFD58a6D08371c") } doReturn workerResponse
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        viewModel = HomeViewModel(repository, resourceAdapter, keyValueDataStore).apply {
            workers.observeForever(testObserver)
        }

        launch {
            // When
            viewModel.fetchWorkers()

            // Then
            testObserver.assertSingleEmission(workerList)
            testObserver.assertTotalEmissions(1)
        }
    }

    @Test
    fun `when fetching workers fails will emit an error event`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<List<Worker>> = LiveDataTestObserver()
        val singleLiveEventObserver: LiveDataTestObserver<HomeNavigation> = LiveDataTestObserver()

        val errorMessage = "Some general error"
        val exception = Exception("Any Exception")
        val apiService = mock<ApiService> {
            onBlocking { fetchWorkers("B3C196D1B394c688c889dE96d1bFD58a6D08371c") } doAnswer {
                throw exception
            }
        }

        val localResourceAdapter: ResourceAdapter = mock {
            on { getString(R.string.general_error) }.doReturn(errorMessage)
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        viewModel = HomeViewModel(repository, localResourceAdapter, keyValueDataStore).apply {
            workers.observeForever(testObserver)
            navigationEvents.observeForever(singleLiveEventObserver)
        }

        launch {
            // When
            viewModel.fetchWorkers()

            // Then
            testObserver.assertTotalEmissions(0)
            singleLiveEventObserver.assertTotalEmissions(1)
            viewModel.navigationEvents.value shouldBeEqualTo ShowError(errorMessage)
        }
    }

    @Test
    fun `when fetching statistics will return a success response`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<Stat> = LiveDataTestObserver()
        val stat = Stat(
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

        val statResponse = ResponseDataObject(
            "OK", stat
        )
        val apiService = mock<ApiService> {
            onBlocking { fetchCurrentStats("B3C196D1B394c688c889dE96d1bFD58a6D08371c") } doReturn statResponse
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        viewModel = HomeViewModel(repository, resourceAdapter, keyValueDataStore).apply {
            stats.observeForever(testObserver)
        }

        launch {
            // When
            viewModel.fetchStatistics()

            // Then
            testObserver.assertSingleEmission(stat)
            testObserver.assertTotalEmissions(1)
        }
    }

    @Test
    fun `when fetching stats fails will emit an error event`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<Stat> = LiveDataTestObserver()
        val singleLiveEventObserver: LiveDataTestObserver<HomeNavigation> = LiveDataTestObserver()

        val errorMessage = "Some general error"
        val exception = Exception("IO Exception")
        val apiService = mock<ApiService> {
            onBlocking { fetchCurrentStats("B3C196D1B394c688c889dE96d1bFD58a6D08371c") } doAnswer {
                throw exception
            }
        }
        val localResourceAdapter: ResourceAdapter = mock {
            on { getString(R.string.general_error) }.doReturn(errorMessage)
        }
        val repository = RepositoryImpl(apiService, dispatcher)

        viewModel = HomeViewModel(repository, localResourceAdapter, keyValueDataStore).apply {
            stats.observeForever(testObserver)
            navigationEvents.observeForever(singleLiveEventObserver)
        }

        launch {
            // When
            viewModel.fetchStatistics()

            // Then
            testObserver.assertTotalEmissions(0)
            singleLiveEventObserver.assertTotalEmissions(1)
            viewModel.navigationEvents.value shouldBeEqualTo ShowError(errorMessage)
        }
    }
}