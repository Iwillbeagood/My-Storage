package com.example.mystorage.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.R

class HomeNameChangeAdapter(private val data: List<Pair<String, String>>) : RecyclerView.Adapter<HomeNameChangeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_user_str_change, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.strNameView.text = "${item.first} : "
        holder.strChangeEditText.setText(item.second)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val strNameView: TextView = itemView.findViewById(R.id.str_name_view)
        val strChangeEditText: EditText = itemView.findViewById(R.id.str_change_editText)
    }
}