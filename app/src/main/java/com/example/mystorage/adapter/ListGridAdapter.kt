package com.example.mystorage.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystorage.R
import com.example.mystorage.retrofit.model.UserItem
import com.example.mystorage.utils.Constants.TAG


class ListGridAdapter(private var data: List<Pair<UserItem, Boolean>>) : RecyclerView.Adapter<ListGridAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "ListGridAdapter - onCreateViewHolder() called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_item, parent, false)
        return ViewHolder(view)
    }

    var isLongClickMode = false

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position].first
        val check = data[position].second

        holder.checkbox.visibility = if (isLongClickMode) View.VISIBLE else View.GONE

        if (check) {
            holder.constraintLayout.setBackgroundResource(R.drawable.brown_bold_bolder)
            holder.checkbox.isChecked = true
        } else {
            holder.constraintLayout.setBackgroundResource(R.drawable.bolder)
            holder.checkbox.isChecked = false
        }

        holder.listItemName.text = item.itemname
        holder.listItemPlace.text = "장소 : " + item.itemplace
        holder.listItemStore.text = "구매처 : " + item.itemstore
        holder.listItemCount.text = item.itemcount

        if (item.itemimage == "") {
            Log.d(TAG, "ListGridAdapter - onBindViewHolder() itemimage is null")
            holder.listItemImage.setImageResource(R.drawable.box)
        } else {
            // 이미지 다운로드 및 설정
            Glide.with(holder.itemView.context)
                .load(item.itemimage)
                .error(R.drawable.box)
                .into(holder.listItemImage)
        }

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(it, position)
        }
        holder.itemView.setOnLongClickListener {

            longClickListener.onItemLongClick(it, position)
            true // 이벤트 소비를 알립니다.
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listItemName: TextView = itemView.findViewById(R.id.listItemName)
        val listItemImage: ImageView = itemView.findViewById(R.id.listItemImage)
        val listItemPlace: TextView = itemView.findViewById(R.id.listItemPlace)
        val listItemStore: TextView = itemView.findViewById(R.id.listItemStore)
        val listItemCount: TextView = itemView.findViewById(R.id.listItemCount)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)


    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    private lateinit var clickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.clickListener = onItemClickListener
    }


    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    private lateinit var longClickListener: OnItemLongClickListener

    fun setItemLongClickListener(listener: OnItemLongClickListener) {
        this.longClickListener = listener
    }


    fun updateItemList(newItemList: List<Pair<UserItem, Boolean>>) {
        data = newItemList.toMutableList()
        notifyDataSetChanged()
    }
}
