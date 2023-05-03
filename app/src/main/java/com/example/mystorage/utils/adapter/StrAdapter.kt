package com.example.mystorage.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.R
import com.example.mystorage.retrofit.response.UserItem
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.GridSpacingItemDecoration

class StrAdapter(private var data: List<List<UserItem>>) : RecyclerView.Adapter<StrAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_str_item, parent, false)
        return StrAdapter.ViewHolder(view)
    }
    override fun onBindViewHolder(holder: StrAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.itemPlaceName.text = item.first().itemplace

        val itemRecyclerView = holder.rvGridInStrItem
        val itemLayoutManager = GridLayoutManager(itemRecyclerView.context, 4)
        itemRecyclerView.layoutManager = itemLayoutManager
        val itemAdapter = StrItemGridAdapter(item)
        itemRecyclerView.adapter = itemAdapter

    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemPlaceName: TextView = itemView.findViewById(R.id.itemPlaceName)
        val rvGridInStrItem: RecyclerView = itemView.findViewById(R.id.RVGridInStrItem)
    }

    fun updateItemList(newItemList: List<List<UserItem>>) {
        data = newItemList.toMutableList()
        notifyDataSetChanged()
    }
}