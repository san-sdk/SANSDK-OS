package com.san.sansample

import androidx.multidex.MultiDexApplication
import com.san.api.SanAdSdk

class SampleApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initAdSdk()
    }

    private fun initAdSdk() {
        SanAdSdk.init(this)
    }
}