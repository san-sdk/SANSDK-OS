package com.san.sansample.holder

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.sansample.R
import com.san.sansample.data.ListData

class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mRootView: ViewGroup? = null

    init {
        mRootView = itemView.findViewById(R.id.root)
    }

    fun onBindViewHolder(data: ListData) {
        if (mRootView != null && data.value.startsWith("#")) {
            mRootView!!.setBackgroundColor(Color.parseColor(data.value))
        }
    }
}