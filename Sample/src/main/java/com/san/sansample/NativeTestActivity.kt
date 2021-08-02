package com.san.sansample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.sansample.adapter.NativeAdListAdapter
import com.san.sansample.data.ListData
import java.util.*

class NativeTestActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_recycle)
        setTitle("Native 信息流测试页面")
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        val adListAdapter = NativeAdListAdapter(this, listData)
//        adListAdapter.setNativePlacementId("1164")//For test
        recyclerView.adapter = adListAdapter

    }

    private val listData: List<ListData>
        get() {
            val listData: MutableList<ListData> = ArrayList()
            listData.add(ListData("#c06f98"))
            listData.add(ListData("#61649f"))
            listData.add(ListData("AD"))
            listData.add(ListData("#c06f98"))
            listData.add(ListData("#61649f"))
            listData.add(ListData("AD"))
            listData.add(ListData("#c06f98"))
            listData.add(ListData("#61649f"))
            listData.add(ListData("AD"))
            listData.add(ListData("#c06f98"))
            listData.add(ListData("#61649f"))
            listData.add(ListData("AD"))
            listData.add(ListData("#c06f98"))
            listData.add(ListData("#61649f"))
            listData.add(ListData("AD"))
            listData.add(ListData("#c06f98"))
            listData.add(ListData("#61649f"))
            return listData
        }

    companion object {
        fun startToNativeTestActivity(context: Context) {
            Log.i("NativeTestActivity", "#startToNativeTestActivity")
            val intent = Intent(context, NativeTestActivity::class.java)
            context.startActivity(intent)
        }
    }
}