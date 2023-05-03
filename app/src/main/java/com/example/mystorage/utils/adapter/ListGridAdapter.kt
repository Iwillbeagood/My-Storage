package com.example.mystorage.utils.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystorage.R
import com.example.mystorage.retrofit.response.UserItem
import com.example.mystorage.utils.Constants.TAG


class ListGridAdapter(private var data: List<UserItem>) : RecyclerView.Adapter<ListGridAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "ListGridAdapter - onCreateViewHolder() called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.listItemName.text = item.itemname
        holder.listItemPlace.text = "장소 : " + item.itemplace
        holder.listItemStore.text = "구매처 : " + item.itemstore
        holder.listItemCount.text = item.itemcount

        if (item.itemimage.isNullOrBlank()) {
            Log.d(TAG, "ListGridAdapter - onBindViewHolder() itemimage is null")
            holder.listItemImage.setImageResource(R.drawable.box)
        } else {
            // 이미지 다운로드 및 설정
            Glide.with(holder.itemView.context)
                .load(item.itemimage)
                .into(holder.listItemImage)
        }

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listItemName: TextView = itemView.findViewById(R.id.listItemName)
        val listItemImage: ImageView = itemView.findViewById(R.id.listItemImage)
        val listItemPlace: TextView = itemView.findViewById(R.id.listItemPlace)
        val listItemStore: TextView = itemView.findViewById(R.id.listItemStore)
        val listItemCount: TextView = itemView.findViewById(R.id.listItemCount)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.clickListener = onItemClickListener
    }

    private lateinit var clickListener: OnItemClickListener

    fun updateItemList(newItemList: List<UserItem>) {
        data = newItemList.toMutableList()
        notifyDataSetChanged()
    }
}
