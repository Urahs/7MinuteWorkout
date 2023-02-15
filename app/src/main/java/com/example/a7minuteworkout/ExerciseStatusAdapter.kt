package com.example.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>): RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemExerciseStatusBinding): RecyclerView.ViewHolder(binding.root){
        val itemTV = binding.itemTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        holder.itemTV.text = model.getId().toString()

        when{
            model.getIsSelected() -> {
                holder.itemTV.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_thin_color_accent_border)
                holder.itemTV.setTextColor(Color.parseColor("#212121"))
            }
            model.getIsCompleted() -> {
                holder.itemTV.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_color_accent_bg)
                holder.itemTV.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                holder.itemTV.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_color_gray_bg)
                holder.itemTV.setTextColor(Color.parseColor("#212121"))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}