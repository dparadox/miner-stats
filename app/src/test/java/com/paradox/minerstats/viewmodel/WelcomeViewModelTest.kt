package com.paradox.minerstats.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import kotlinx.coroutines.test.*
import org.junit.Test
import com.nhaarman.mockitokotlin2.mock
import com.paradox.minerstats.LiveDataTestObserver
import com.paradox.minerstats.R
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.adapter.ResourceAdapter
import com.paradox.minerstats.core.config.PrefKeys.KEY_WALLET_ADDRESS
import com.paradox.minerstats.ui.welcome.WelcomeViewModel
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation.ShowError
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class WelcomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val keyValueDataStore = mock<KeyValueDataStore>()
    private lateinit var viewModel: WelcomeViewModel

    @Test
    fun `when entering an invalid address will emit an error`() = runTest {
        // Given
        val errorMessage = "Any error message"
        val testObserver: LiveDataTestObserver<WelcomeNavigation> = LiveDataTestObserver()
        val resourceAdapter: ResourceAdapter = mock {
            on { getString(R.string.address_wallet_invalid) }.doReturn(errorMessage)
        }

        viewModel = WelcomeViewModel(resourceAdapter, keyValueDataStore).apply {
            navigationEvents.observeForever(testObserver)
        }

        viewModel.inputWallet.value = "Something"

        // When
        viewModel.onGetStartedTapped()

        // Then
        testObserver.assertTotalEmissions(1)
        viewModel.navigationEvents.value shouldBeEqualTo ShowError(errorMessage)
    }

    @Test
    fun `when entering a valid address will emit success`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<WelcomeNavigation> = LiveDataTestObserver()
        val resourceAdapter: ResourceAdapter = mock()

        viewModel = WelcomeViewModel(resourceAdapter, keyValueDataStore).apply {
            navigationEvents.observeForever(testObserver)
        }

        viewModel.inputWallet.value = "B3C196D1B394c688c889dE96d1bFD58a6D08371c"

        // When
        viewModel.onGetStartedTapped()

        // Then
        testObserver.assertTotalEmissions(1)
        viewModel.navigationEvents.value shouldBeEqualTo Success
    }

    @Test
    fun `when fetching an existing address will emit success`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<WelcomeNavigation> = LiveDataTestObserver()
        val resourceAdapter: ResourceAdapter = mock()

        val keyValueDataStore: KeyValueDataStore = mock {
            on { getString(KEY_WALLET_ADDRESS) }.doReturn("B3C196D1B394c688c889dE96d1bFD58a6D08371c")
        }

        viewModel = WelcomeViewModel(resourceAdapter, keyValueDataStore).apply {
            navigationEvents.observeForever(testObserver)
        }

        // When
        viewModel.fetchExistingLocalWallet()

        // Then
        testObserver.assertTotalEmissions(1)
        viewModel.navigationEvents.value shouldBeEqualTo Success
    }

    @Test
    fun `when there is not saved address should never emit success`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<WelcomeNavigation> = LiveDataTestObserver()
        val resourceAdapter: ResourceAdapter = mock()

        val keyValueDataStore: KeyValueDataStore = mock {
            on { getString(KEY_WALLET_ADDRESS) }.doReturn("")
        }

        viewModel = WelcomeViewModel(resourceAdapter, keyValueDataStore).apply {
            navigationEvents.observeForever(testObserver)
        }

        // When
        viewModel.fetchExistingLocalWallet()

        // Then
        testObserver.assertTotalEmissions(0)
    }
}