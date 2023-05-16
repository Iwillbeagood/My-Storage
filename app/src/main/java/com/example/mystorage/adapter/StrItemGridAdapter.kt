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
import com.example.mystorage.utils.Constants.TAG

class StrItemGridAdapter(private var data: List<UserItem>) : RecyclerView.Adapter<StrItemGridAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_str_item_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.strItemName.text = item.itemname

        if (item.itemimage == "") {
            holder.strItemImage.setImageResource(R.drawable.box)
        } else {
            // 이미지 다운로드 및 설정
            Glide.with(holder.itemView.context)
                .load(item.itemimage)
                .error(R.drawable.box)
                .into(holder.strItemImage)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val strItemName: TextView = itemView.findViewById(R.id.strItemName)
        val strItemImage: ImageView = itemView.findViewById(R.id.strItemImage)
    }
}