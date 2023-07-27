package com.example.newdiplom.domain.entities.customRecyclerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newdiplom.R
import com.example.newdiplom.domain.presentation.taskInf
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(
    private val Taskinformation: ArrayList<taskInf>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return Taskinformation.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = Taskinformation[position]
        holder.titleImage.setImageResource(currentItem.titleImage)
        holder.heading1.text = currentItem.nameTask
        holder.heading2.text = currentItem.dateTask

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleImage: ShapeableImageView = itemView.findViewById(R.id.title_image)
        val heading1: TextView = itemView.findViewById(R.id.tvHeading)
        val heading2: TextView = itemView.findViewById(R.id.tvHeading2)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}