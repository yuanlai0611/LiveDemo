package com.example.livedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_live.view.*

class LiveListAdapter(private val ctx: Context, private var liveDataItems: List<LiveDataItem>): RecyclerView.Adapter<LiveViewHolder>() {
    private var itemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Int) -> Unit)?) {
        itemClickListener = listener
    }

    fun setData(dataItems: List<LiveDataItem>) {
        liveDataItems = dataItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveViewHolder {
        return LiveViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_live, parent, false))
    }

    override fun getItemCount(): Int {
        return liveDataItems.size
    }

    override fun onBindViewHolder(holder: LiveViewHolder, position: Int) {
        holder.bind(liveDataItems[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.let { it1 -> it1(position) }
        }
    }
}

class LiveViewHolder(private val v: View): RecyclerView.ViewHolder(v) {
    fun bind(dataItem: LiveDataItem) {
        v.typeTv.text = dataItem.type
        v.numTv.text = dataItem.num.toString()
        v.usrTv.text = dataItem.title
        v.titleTv.text = dataItem.title
        v.coverRiv.setImageDrawable(v.resources.getDrawable(R.drawable.ic_launcher_background))
        v.usrRiv.setImageDrawable(v.resources.getDrawable(R.mipmap.ic_launcher_round))
    }
}