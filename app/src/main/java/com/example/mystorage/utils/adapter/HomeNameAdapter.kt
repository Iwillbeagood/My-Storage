package com.example.mystorage.utils.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.R
import com.example.mystorage.utils.Constants.TAG


class HomeNameAdapter(private val data: List<String>, private val type: String) : RecyclerView.Adapter<HomeNameAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNameAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_user_strname, parent, false)
        return HomeNameAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeNameAdapter.ViewHolder, position: Int) {
        val item = data[position]

        if (type == "room"){
            holder.strImageView.setImageResource(R.drawable.ic_baseline_sensor_door_24)
        } else if (type == "rest") {
            holder.strImageView.setImageResource(R.drawable.bathroom_cleaning_housekeeping_toilet_svgrepo_com)
        }

        holder.strEditText.setText(item)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val strImageView: ImageView = itemView.findViewById(R.id.strImageView)
        val strEditText: EditText = itemView.findViewById(R.id.strEditText)
    }
}