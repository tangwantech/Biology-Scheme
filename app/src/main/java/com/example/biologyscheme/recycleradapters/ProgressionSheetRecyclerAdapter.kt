package com.example.biologyscheme.recycleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biologyscheme.R
import com.example.biologyscheme.databinding.ProgressionSheetRecyclerItemBinding
import com.example.biologyscheme.models.ProgressionSheetData

class ProgressionSheetRecyclerAdapter(
    private val context: Context,
    private val onRecyclerItemClickListener: OnRecyclerItemClickListener,
    private val onItemCheckStateChangeListener: OnItemCheckStateChangeListener): RecyclerView.Adapter<ProgressionSheetRecyclerAdapter.ViewHolder>() {
    private var data = ArrayList<ProgressionSheetData>()

    fun updateData(temp: ArrayList<ProgressionSheetData>){
        data = temp
    }

    inner class ViewHolder(val binding: ProgressionSheetRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        init {

            binding.main.setOnClickListener {
                onRecyclerItemClickListener.onItemClicked(adapterPosition)
            }

            binding.checkBoxTopic.setOnCheckedChangeListener { compoundButton, checkState ->
                onItemCheckStateChangeListener.onItemCheckStateChanged(adapterPosition, checkState)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProgressionSheetRecyclerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTopic.text = context.getString(R.string.topic, (position + 1).toString(), data[position].topicName)
        holder.binding.tvDateRange.text = context.getString(R.string.date_range, data[position].startDate, data[position].endDate)
        holder.binding.checkBoxTopic.isChecked = data[position].isTaught
    }

    interface OnRecyclerItemClickListener{
        fun onItemClicked(itemPosition: Int)
    }

    interface OnItemCheckStateChangeListener{
        fun onItemCheckStateChanged(itemPosition: Int, checkState: Boolean)
    }


}