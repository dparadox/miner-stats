package com.paradox.minerstats

import androidx.lifecycle.Observer

/**
 * LiveDataTestObserver
 *
 * This class is for testing livedata variables and emissions
 * @author Esteban Lopez
 * @date 05-08-2018
 *
 * Use observeForever in your LiveData
 */

class LiveDataTestObserver<T> : Observer<T> {
    private val observedData = ArrayList<T?>()

    override fun onChanged(t: T?) {
        observedData.add(t)
    }

    fun assertSingleEmission(expectedEmission: T?) {
        assert(observedData.size == 1) {
            "Expected the number of observed emissions to be 1, but was ${observedData.size}"
        }
        assert(observedData[0] == expectedEmission) {
            "Expected a single emission of $expectedEmission, but was ${observedData[0]}"
        }
    }

    fun assertLastEmission(expectedEmission: T?)  {
        assert(observedData.size > 0) {
            "Expected 1 or more emissions, but there were none."
        }
        assert(expectedEmission == observedData.last()) {
            "Expected the last emission to equal $expectedEmission, but was ${observedData.last()}"
        }
    }

    fun assertTotalEmissions(expectedEmissionsCount: Int)  {
        assert(expectedEmissionsCount == observedData.size) {
            "Expected total number of emissions to be $expectedEmissionsCount, but was ${observedData.size}"
        }
    }
}