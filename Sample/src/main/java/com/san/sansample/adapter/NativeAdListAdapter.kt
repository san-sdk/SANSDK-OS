package com.san.sansample.adapter

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.ads.AdError
import com.san.ads.SANNativeAd
import com.san.ads.base.IAdListener
import com.san.ads.core.SANAd
import com.san.sansample.BuildConfig
import com.san.sansample.R
import com.san.sansample.data.ListData
import com.san.sansample.holder.EmptyViewHolder
import com.san.sansample.holder.NativeAdViewHolder

class NativeAdListAdapter(private val mContext: Context, private var mDataList: List<ListData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mOnItemClickListener: OnItemClickListener? = null
    private var pid: String = BuildConfig.placement_id_native

    fun setNativePlacementId(placementId: String){
        pid = placementId
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        val value = mDataList[position].value
        return if (TextUtils.equals(value, "AD")) AD else NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_normal_view, parent, false)
        return if (viewType == AD) NativeAdViewHolder(view) else EmptyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == AD) {
            val holder = viewHolder as NativeAdViewHolder
            holder.onBindViewHolder(mDataList[position])
            tryToLoadNativeAd(position)
        } else if (viewType == NORMAL) {
            val holder = viewHolder as EmptyViewHolder
            holder.onBindViewHolder(mDataList[position])
        }
        setItemClickListener(viewHolder, position)
    }

    private fun tryToLoadNativeAd(position: Int) {
        if (mDataList[position].isAdLoaded) {
            return
        }
        Log.d(TAG, "#tryToLoadNativeAd position = $position")
        val nativeAd = SANNativeAd(mContext, pid)
        nativeAd.setAdLoadListener(AdLoadListenerWrapper(position))
        nativeAd.load()
    }

    private inner class AdLoadListenerWrapper(var position: Int) : IAdListener.AdLoadListener {
        override fun onAdLoaded(adWrapper: SANAd) {
            Log.i(TAG, "#onAdLoaded position = $position")
            if (adWrapper is SANNativeAd) {
                mDataList[position].nativeAd = adWrapper
                mDataList[position].isAdLoaded = true

                Handler().post {
                    notifyItemChanged(position)
                }
            }
        }

        override fun onAdLoadError(adError: AdError) {
            Log.i(TAG, "#onAdLoadError position = " + position + ", adError = " + adError.errorMessage)
        }
    }

    private fun setItemClickListener(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder.itemView.setOnClickListener { v: View? -> mOnItemClickListener?.onItemClick(position) }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    companion object {
        private const val TAG = "ListAdapter"
        const val NORMAL = 1
        const val AD = 2
    }
}