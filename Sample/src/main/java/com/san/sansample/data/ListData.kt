package com.san.sansample.data

import com.san.ads.SANNativeAd

data class ListData(var value: String) {
    var nativeAd: SANNativeAd? = null
    var isAdLoaded = false
}