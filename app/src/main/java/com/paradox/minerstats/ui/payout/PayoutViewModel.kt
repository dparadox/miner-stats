package com.paradox.minerstats.ui.payout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paradox.minerstats.R
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.adapter.ResourceAdapter
import com.paradox.minerstats.core.config.PrefKeys
import com.paradox.minerstats.model.dto.Payout
import com.paradox.minerstats.model.repository.Repository
import com.paradox.minerstats.ui.event.SingleLiveEvent
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayoutViewModel @Inject constructor(
    private val repository: Repository,
    private val resourceAdapter: ResourceAdapter,
    private val keyValueDataStore: KeyValueDataStore
) : ViewModel() {

    val navigationEvents = SingleLiveEvent<PayoutNavigation>()
    val payouts = MutableLiveData<List<Payout>>()
    val inProgress = MutableLiveData<Boolean>()

    fun fetchPayouts() {
        val miner = keyValueDataStore.getString(PrefKeys.KEY_WALLET_ADDRESS)!!
        inProgress.postValue(true)

        viewModelScope.launch {
            repository.fetchPayouts(miner).collect {
                inProgress.postValue(false)
                when {
                    it.isSuccess -> {
                        payouts.value = it.getOrNull()?.data.orEmpty()
                    }
                    it.isFailure -> {
                        navigationEvents.postValue(
                            ShowError(
                                resourceAdapter.getString(
                                    R.string.general_error
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun onExploreTapped(txId: String) {
        navigationEvents.postValue(
            ShowPayoutDetail(
                resourceAdapter.getString(
                    R.string.ether_scan_tx_uri,
                    txId
                )
            )
        )
    }
}