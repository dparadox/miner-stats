package com.paradox.minerstats.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.*
import org.junit.Test
import org.junit.Before
import com.paradox.minerstats.LiveDataTestObserver
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.ui.settings.SettingsViewModel
import com.paradox.minerstats.ui.settings.navigation.SettingsNavigation
import com.paradox.minerstats.ui.settings.navigation.SettingsNavigation.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val keyValueDataStore: KeyValueDataStore = mock()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(keyValueDataStore)
    }

    @Test
    fun `when tapping on logout should remove saved wallet and emit a success event`() = runTest {
        // Given
        val testObserver: LiveDataTestObserver<SettingsNavigation> = LiveDataTestObserver()
        viewModel.navigationEvents.observeForever(testObserver)

        // When
        viewModel.onLogoutTapped()

        // Then
        verify(keyValueDataStore).remove(any())
        testObserver.assertTotalEmissions(1)
        viewModel.navigationEvents.value shouldBeEqualTo Success
    }
}