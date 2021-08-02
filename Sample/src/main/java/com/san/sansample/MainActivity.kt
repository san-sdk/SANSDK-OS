package com.san.sansample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.san.ads.*
import com.san.ads.base.FullScreenAdWrapper
import com.san.ads.base.IAdListener
import com.san.ads.core.SANAd
import com.san.ads.render.AdViewRenderHelper
import com.san.ads.render.SANNativeAdRenderer
import com.san.mads.nativead.MadsNativeAd
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var TAG: String = "MainActivity"
    private var mContainer: FrameLayout? = null
    private var mNativeContainer: FrameLayout? = null
    private var itlAd: SANInterstitial? = null
    private var rwdAd: SANReward? = null
    private var bannerAd_50: SANBanner? = null
    private var bannerAd_250: SANBanner? = null

    private val PID_INTER: String = BuildConfig.placement_id_itl
    private val PID_RWD: String = BuildConfig.placement_id_rwd
    private val PID_BANNER_50: String = BuildConfig.placement_id_banner_50
    private val PID_BANNER_250: String = BuildConfig.placement_id_banner_250

    private val PID_NATIVE: String = BuildConfig.placement_id_native

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContainer = banner_container as FrameLayout
        mNativeContainer = native_render_container as FrameLayout

        initNative()

        initInterstitial()

        initRewarded()

        initBanner()

        btn_native_test.setOnClickListener {
            NativeTestActivity.startToNativeTestActivity(this)
        }
    }

    private fun initNative() {
        btn_native.setOnClickListener {
            Log.d(TAG, "#requestAd start")
            val nativeAd = SANNativeAd(this, PID_NATIVE)

            nativeAd.setAdLoadListener(object : IAdListener.AdLoadListener {

                override fun onAdLoaded(madsAd: SANAd?) {
                    madsAd as BaseNativeAd;
                    Log.d(TAG, "#requestNativeAd AdLoaded")
                    renderNativeAd(madsAd)
//                    rendererAdViewForMediation(madsAd);
                }

                override fun onAdLoadError(adError: AdError?) {
                    Log.d(TAG, "#requestNative onAdLoadError $adError")
                }

            })
            nativeAd.setAdActionListener(mNativeAdListener)
            nativeAd.load()
        }
    }

    private val mNativeAdListener: IAdListener.AdActionListener = object : IAdListener.AdActionListener {
        override fun onAdImpressionError(error: AdError) {
            Log.d(TAG, "#onAdNativeImpressionError $error")

        }
        override fun onAdImpression() {
            Log.d(TAG, "onAdNativeImpression")

        }
        override fun onAdClicked() {
            Log.d(TAG, "#onAdClicked")

        }
        override fun onAdCompleted() {
            Log.d(TAG, "#onAdCompleted")

        }
        override fun onAdClosed(hasRewarded: Boolean) {
            Log.d(TAG, "#onAdClosed hasRewarded = $hasRewarded")

        }
    }

    private fun renderNativeAd(nativeAd: BaseNativeAd) {
        val contentView = LayoutInflater.from(this).inflate(R.layout.ad_native_layout, mNativeContainer, false)
        val titleText = contentView.findViewById<TextView>(R.id.native_title)
        val contentText = contentView.findViewById<TextView>(R.id.native_text)
        val buttonView = contentView.findViewById<TextView>(R.id.native_button)
        val iconImage = contentView.findViewById<ImageView>(R.id.native_icon_image)
        val mediaLayout: MediaView = contentView.findViewById(R.id.native_main_image)

        //text
        titleText.text = nativeAd.title
        contentText.text = nativeAd.content
        buttonView.text = nativeAd.callToAction
        //icon
        AdViewRenderHelper.loadImage(iconImage.context, nativeAd.iconUrl, iconImage)
        //media view
        mediaLayout.loadMadsMediaView(nativeAd.nativeAd as MadsNativeAd)
        //click list
        val clickViews: MutableList<View> = ArrayList()
        clickViews.add(titleText)
        clickViews.add(contentText)
        clickViews.add(buttonView)
        clickViews.add(iconImage)
        clickViews.add(mediaLayout)
        //prepare
        nativeAd.prepare(contentView, clickViews, null)
        mNativeContainer?.removeAllViews()
        mNativeContainer?.addView(contentView)
    }

    private fun rendererAdViewForMediation(nativeAd: BaseNativeAd) {
        mNativeContainer?.removeAllViews()

        nativeAd as SANNativeAd
        val adRenderer = SANNativeAdRenderer(
            SANNativeAdRenderer.SViewBinder.Builder(R.layout.ad_item_layout_mediation)
                .iconImageId(R.id.native_icon_image)
                .mainImageId(R.id.native_main_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_button)
                .build()
        )
        val adView: View = adRenderer.createAdView(this, nativeAd, null)
        adRenderer.renderAdView(adView, nativeAd)
        mNativeContainer?.addView(adView)
    }

    private fun initInterstitial() {
        btn_request_itl.setOnClickListener {
            Log.d(TAG, "#requestAd start")
            requestInterstitialAd(PID_INTER)//插屏
        }
        btn_show_itl.setOnClickListener {
            showInterstitialAd()
        }
    }

    private fun initRewarded() {
        btn_request_rwd.setOnClickListener {
            Log.d(TAG, "#requestAd start")
            requestRewardedAd(PID_RWD)// 激励
        }
        btn_show_rwd.setOnClickListener {
            showRewardedAd()
        }
    }

    private fun initBanner() {
        btn_request_banner.setOnClickListener {
            Log.d(TAG, "#requestAd start")
            requestBannerAd50(PID_BANNER_50)
        }
        btn_show_banner.setOnClickListener {
            showBannerAd()
        }

        btn_request_banner250.setOnClickListener {
            Log.d(TAG, "#requestAd start")
            requestBannerAd250(PID_BANNER_250)
        }
        btn_show_banner250.setOnClickListener {
            showBannerAd250()
        }
    }

    private fun requestInterstitialAd() {
        requestInterstitialAd(PID_INTER)
    }

    private fun requestInterstitialAd(pid: String) {
        itlAd = SANInterstitial(this, pid)
        itlAd?.setAdLoadListener(object : IAdListener.AdLoadListener {

            override fun onAdLoaded(madsAd: SANAd?) {
                Log.d(TAG, "#requestInterstitialAd AdLoaded")
                if (madsAd is SANInterstitial) {
                    Toast.makeText(applicationContext, "Interstitial Loaded", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAdLoadError(adError: AdError?) {
                Log.d(TAG, "#requestInterstitialAd onAdLoadError $adError")
            }

        })
        itlAd?.setAdActionListener(object : IAdListener.AdActionListener {
            override fun onAdImpressionError(error: AdError?) {
                Log.d(TAG, "#onAdImpressionError $error")
            }

            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression")
            }

            override fun onAdClicked() {
                Log.d(TAG, "#onAdClicked")
            }

            override fun onAdCompleted() {
                Log.d(TAG, "#onAdCompleted")
            }

            override fun onAdClosed(hasRewarded: Boolean) {
                Log.d(TAG, "#onAdClosed hasRewarded = $hasRewarded")
            }
        })
        itlAd?.load()
        Log.d(TAG, "#itlAd end")
    }

    private fun showInterstitialAd() {
        if (itlAd == null) {
            requestInterstitialAd()
            return
        }
        if (itlAd!!.isAdReady) {
            itlAd?.show()
        } else {
            Toast.makeText(applicationContext, "Ad not ready, will reload", Toast.LENGTH_SHORT).show()
            itlAd?.load()
        }
    }

    private fun requestRewardedAd() {
        requestRewardedAd(PID_RWD)
    }

    private fun requestRewardedAd(pid: String) {
        rwdAd = SANReward(this, pid)
        rwdAd?.setAdLoadListener(object : IAdListener.AdLoadListener {

            override fun onAdLoaded(madsAd: SANAd?) {
                Log.d(TAG, "#requestRewardedAd onRewardedVideoAdLoaded")
                if (madsAd is FullScreenAdWrapper) {
                    Toast.makeText(applicationContext, "Rewarded Loaded", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAdLoadError(adError: AdError?) {
                Log.d(TAG, "#requestRewardedAd onAdLoadError $adError")
            }

        })
        rwdAd?.setAdActionListener(object : IAdListener.AdActionListener {
            override fun onAdImpressionError(error: AdError?) {
                Log.d(TAG, "#onAdImpressionError $error")
            }

            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression")
            }

            override fun onAdClicked() {
                Log.d(TAG, "#onAdClicked")
            }

            override fun onAdCompleted() {
                Log.d(TAG, "#onAdCompleted")
            }

            override fun onAdClosed(hasRewarded: Boolean) {
                Log.d(TAG, "#onAdClosed hasRewarded = $hasRewarded")
            }
        })
        rwdAd?.load()
        Log.d(TAG, "#rewarded end")
    }

    private fun showRewardedAd() {
        if (rwdAd == null) {
            requestRewardedAd()
            return
        }
        if (rwdAd!!.isAdReady) {
            rwdAd?.show()
        } else {
            Toast.makeText(applicationContext, "Ad not ready, will reload", Toast.LENGTH_SHORT).show()
            rwdAd?.load()
        }
    }

    private fun requestBannerAd50() {
        requestBannerAd50(PID_BANNER_50)
    }

    private fun requestBannerAd50(pid: String) {
        bannerAd_50 = SANBanner(this, pid)
        bannerAd_50?.adSize = AdSize.BANNER

        val listener = getBannerAdLoadListener()
        Log.d(TAG, "##requestBannerAd50 listener = $listener")

        bannerAd_50?.setAdLoadListener(listener)

        bannerAd_50?.load()
    }

    private fun showBannerAd() {
        if (bannerAd_50 == null) {
            requestBannerAd50()
            return
        }
        bannerAd_50?.setAdActionListener(object : IAdListener.AdActionListener {
            override fun onAdImpressionError(error: AdError?) {
                Log.d(TAG, "#onAdImpressionError $error")
            }

            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression")
            }

            override fun onAdClicked() {
                Log.d(TAG, "#onAdClicked")
            }

            override fun onAdCompleted() {
                Log.d(TAG, "#onAdCompleted")
            }

            override fun onAdClosed(hasRewarded: Boolean) {
                Log.d(TAG, "#onAdClosed hasRewarded = $hasRewarded")
            }
        })
        if (bannerAd_50 != null && bannerAd_50!!.isAdReady && mContainer != null) {
            mContainer!!.removeAllViews()
            mContainer!!.addView(bannerAd_50!!.adView)
        } else {
            Toast.makeText(applicationContext, "Ad not ready, will reload", Toast.LENGTH_SHORT).show()

            requestBannerAd50("1133")
        }
    }

    private fun requestBannerAd250() {
        requestBannerAd50(PID_BANNER_250)
    }

    private fun requestBannerAd250(pid: String) {
        bannerAd_250 = SANBanner(this, pid)
        bannerAd_250?.adSize = AdSize.MEDIUM_RECTANGLE

        val listener = getBannerAdLoadListener()
        Log.d(TAG, "##requestBannerAd250 listener = $listener")

        bannerAd_250?.setAdLoadListener(listener)
        bannerAd_250?.load()
    }

    private fun showBannerAd250() {
        if (bannerAd_250 == null) {
            requestBannerAd250()
            return
        }
        bannerAd_250?.setAdActionListener(object : IAdListener.AdActionListener {
            override fun onAdImpressionError(error: AdError?) {
                Log.d(TAG, "#onAdImpressionError $error")
            }

            override fun onAdImpression() {
                Log.d(TAG, "onAdImpression")
            }

            override fun onAdClicked() {
                Log.d(TAG, "#onAdClicked")
            }

            override fun onAdCompleted() {
                Log.d(TAG, "#onAdCompleted")
            }

            override fun onAdClosed(hasRewarded: Boolean) {
                Log.d(TAG, "#onAdClosed hasRewarded = $hasRewarded")
            }
        })
        if (bannerAd_250 != null && bannerAd_250!!.isAdReady && mContainer != null) {
            mContainer!!.removeAllViews()
            mContainer!!.addView(bannerAd_250!!.adView)
        } else {
            Toast.makeText(applicationContext, "Ad not ready, will reload", Toast.LENGTH_SHORT).show()
            requestBannerAd250("1132")
        }

    }

    private fun getBannerAdLoadListener() = object : IAdListener.AdLoadListener {

        override fun onAdLoaded(banner: SANAd?) {
            if (banner!!.isAdReady && banner is SANBanner) {
                Toast.makeText(applicationContext, "Banner Loaded", Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "#requestBannerAd onBannerLoaded $this")
        }

        override fun onAdLoadError(adError: AdError?) {
            Log.d(TAG, "##requestBannerAd onAdLoadError $adError $this")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}