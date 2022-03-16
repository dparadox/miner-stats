package com.paradox.minerstats.ui.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paradox.minerstats.R
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.adapter.ResourceAdapter
import com.paradox.minerstats.core.config.PrefKeys.KEY_WALLET_ADDRESS
import com.paradox.minerstats.ui.event.SingleLiveEvent
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation.ShowError
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import org.web3j.crypto.WalletUtils
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val resourceAdapter: ResourceAdapter,
    private val keyValueDataStore: KeyValueDataStore
) : ViewModel() {

    val navigationEvents = SingleLiveEvent<WelcomeNavigation>()
    val inputWallet = MutableLiveData<String>()

    fun fetchExistingLocalWallet() {
        if (!keyValueDataStore.getString(KEY_WALLET_ADDRESS).isNullOrBlank()) {
            navigationEvents.postValue(Success)
        }
    }

    fun onGetStartedTapped() {
        if (inputWallet.value.isNullOrBlank()) {
            navigationEvents.postValue(ShowError(resourceAdapter.getString(R.string.please_enter_valid_address)))
            return
        }
        val wallet = inputWallet.value!!

        if (WalletUtils.isValidAddress(wallet)) {
            keyValueDataStore.putString(KEY_WALLET_ADDRESS, wallet)
            navigationEvents.postValue(Success)
        } else {
            navigationEvents.postValue(
                ShowError(resourceAdapter.getString(R.string.address_wallet_invalid))
            )
        }
    }
}