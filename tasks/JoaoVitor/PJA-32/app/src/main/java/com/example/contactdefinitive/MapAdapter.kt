package com.example.contactdefinitive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class MapAdapter(private val data: Map<String, String>, private val dialog: androidx.appcompat.app.AlertDialog, private val currencyLiveData: MutableLiveData<String>) :
    RecyclerView.Adapter<MapAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = data.keys.toList()[position]
        holder.itemText.text = String.format(Locale("pt", "BR"), "${data[key]} ($key)")
        holder.itemText.setOnClickListener {
            currencyLiveData.postValue(holder.itemText.text.toString())
            dialog.dismiss()
        }
    }

    override fun getItemCount(): Int = data.size
}