package com.jiho.smartfarm_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jiho.smartfarm_kotlin.R

class RecycleAdapter (viewholderlist: MutableList<RecycleData>?) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
    private var Recycle_data : MutableList<RecycleData> ?= viewholderlist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oneRecord = Recycle_data!![position]
        holder.setItem(oneRecord)
    }

    override fun getItemCount(): Int {
        return Recycle_data!!.size
    }

    public fun additem(oneRecord: RecycleData) {
        Recycle_data!!.add(oneRecord)

        notifyDataSetChanged()
    }
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text_date1: TextView
        var text_value1: TextView

        public fun setItem(item: RecycleData) {
            text_date1.text = item.date
            text_value1.text = item.value
        }

        init {
            text_date1 = itemView.findViewById(R.id.text_holder_date)
            text_value1 = itemView.findViewById(R.id.text_holder_value)
        }
    }
}