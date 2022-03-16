package com.paradox.minerstats.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paradox.minerstats.R
import com.paradox.minerstats.core.adapter.KeyValueDataStore
import com.paradox.minerstats.core.adapter.ResourceAdapter
import com.paradox.minerstats.core.config.PrefKeys
import com.paradox.minerstats.model.dto.Stat
import com.paradox.minerstats.model.dto.Worker
import com.paradox.minerstats.model.repository.Repository
import com.paradox.minerstats.ui.event.SingleLiveEvent
import com.paradox.minerstats.ui.home.navigation.HomeNavigation
import com.paradox.minerstats.ui.home.navigation.HomeNavigation.ShowError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val resourceAdapter: ResourceAdapter,
    private val keyValueDataStore: KeyValueDataStore
) : ViewModel() {

    val navigationEvents = SingleLiveEvent<HomeNavigation>()
    val workers = MutableLiveData<List<Worker>>()
    val stats = MutableLiveData<Stat>()
    val inProgress = MutableLiveData<Boolean>()

    fun fetchWorkers() {
        val miner = keyValueDataStore.getString(PrefKeys.KEY_WALLET_ADDRESS)!!
        inProgress.postValue(true)

        viewModelScope.launch {
            repository.fetchWorkers(miner).collect {
                inProgress.postValue(false)
                when {
                    it.isSuccess -> {
                        workers.value = it.getOrNull()?.data.orEmpty()
                    }
                    it.isFailure -> {
                        // TODO Handle individual exceptions
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

    fun fetchStatistics() {
        val miner = keyValueDataStore.getString(PrefKeys.KEY_WALLET_ADDRESS)!!

        viewModelScope.launch {
            repository.fetchStats(miner).collect {
                when {
                    it.isSuccess -> {
                        it.getOrNull()?.data?.let { statistic ->
                            stats.value = statistic
                        }
                    }
                    it.isFailure -> {
                        // TODO Handle individual exceptions
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
}