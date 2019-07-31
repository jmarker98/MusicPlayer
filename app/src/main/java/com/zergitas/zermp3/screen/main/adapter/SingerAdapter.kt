package com.zergitas.zermp3.screen.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Singer
import kotlinx.android.synthetic.main.item_singer.view.*

class SingerAdapter(
    var singerList: ArrayList<Singer>,
    var context: Context,
    var listener: SingerListener
) : RecyclerView.Adapter<SingerAdapter.SingerViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SingerViewHolder {
        return SingerViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_singer, p0, false))
    }

    override fun getItemCount(): Int {
        return singerList.size
    }

    override fun onBindViewHolder(p0: SingerViewHolder, p1: Int) {
        var singer: Singer = singerList[p1]
        p0.itemView.txt_singerName.text = singer.name
        p0.itemView.item_singer.setOnClickListener({
            listener.onItemClick(singer.id_singer,singer.name)
        })
    }

    class SingerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    interface SingerListener {
        fun onItemClick(id:Int,name: String)
    }
}