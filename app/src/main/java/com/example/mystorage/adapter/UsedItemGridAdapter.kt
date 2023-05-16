package com.example.mystorage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystorage.R
import com.example.mystorage.retrofit.model.UserItem
import com.example.mystorage.utils.CalculateDurationUtil
import com.example.mystorage.utils.Constants

class UsedItemGridAdapter(private var data: List<UserItem>) : RecyclerView.Adapter<UsedItemGridAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(Constants.TAG, "UsedItemGridAdapter - onCreateViewHolder() called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_used_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.usedItemName.text = item.itemname
        holder.usedItemStore.text = "구매처 : " + item.itemstore
        holder.usedItemTime.text = CalculateDurationUtil.calculateDuration(item.created_at.toString())

        if (item.itemimage == "") {
            Log.d(Constants.TAG, "UsedItemGridAdapter - onBindViewHolder() itemimage is null")
            holder.usedItemImage.setImageResource(R.drawable.box)
        } else {
            // 이미지 다운로드 및 설정
            Glide.with(holder.itemView.context)
                .load(item.itemimage)
                .error(R.drawable.box)
                .into(holder.usedItemImage)
        }

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usedItemName: TextView = itemView.findViewById(R.id.usedItemName)
        val usedItemImage: ImageView = itemView.findViewById(R.id.usedItemImage)
        val usedItemStore: TextView = itemView.findViewById(R.id.usedItemStore)
        val usedItemTime: TextView = itemView.findViewById(R.id.usedItemTime)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setUsedItemClickListener(onItemClickListener: OnItemClickListener) {
        this.clickListener = onItemClickListener
    }

    private lateinit var clickListener: OnItemClickListener
}
