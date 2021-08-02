package com.san.sansample.holder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.ads.render.SANNativeAdRenderer
import com.san.ads.render.SANNativeAdRenderer.SViewBinder
import com.san.sansample.R
import com.san.sansample.data.ListData

class NativeAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mNativeContainer: ViewGroup? = null
    fun onBindViewHolder(data: ListData) {
        val nativeAd = data.nativeAd
        if (nativeAd == null) {
            val param = itemView.layoutParams
            param.height = 0
            itemView.layoutParams = param
        } else {
            val param = itemView.layoutParams
            param.height = ViewGroup.LayoutParams.WRAP_CONTENT
            itemView.layoutParams = param
            val adRenderer = SANNativeAdRenderer(
                SViewBinder.Builder(R.layout.ad_item_layout_mediation)
                    .iconImageId(R.id.native_icon_image)
                    .mainImageId(R.id.native_main_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_button)
                    .build()
            )
            val context = mNativeContainer?.context ?: return
            val adView = adRenderer.createAdView(context, nativeAd, null)
            adRenderer.renderAdView(adView, nativeAd)
            mNativeContainer!!.removeAllViews()
            mNativeContainer!!.addView(adView)
        }
    }

    init {
        mNativeContainer = itemView.findViewById(R.id.root)
    }
}