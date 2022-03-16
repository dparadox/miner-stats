package com.paradox.minerstats.ui.settings

import androidx.lifecycle.ViewModel
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.config.PrefKeys.KEY_WALLET_ADDRESS
import com.paradox.minerstats.ui.event.SingleLiveEvent
import com.paradox.minerstats.ui.settings.navigation.SettingsNavigation
import com.paradox.minerstats.ui.settings.navigation.SettingsNavigation.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val keyValueDataStore: KeyValueDataStore
) : ViewModel() {

    val navigationEvents = SingleLiveEvent<SettingsNavigation>()

    fun onLogoutTapped() {
        keyValueDataStore.remove(KEY_WALLET_ADDRESS)
        navigationEvents.postValue(Success)
    }
}