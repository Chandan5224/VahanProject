package com.example.vahanproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vahanproject.R
import com.example.vahanproject.api.UniversityAPI
import com.example.vahanproject.models.University

class UniversityAdapter(private val listener: OnItemsClick) :
    RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    class UniversityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uniName: TextView = itemView.findViewById(R.id.tvUniversityName)
        val uniWebsite: TextView = itemView.findViewById(R.id.tvWebsite)
        val uniCountry: TextView = itemView.findViewById(R.id.tvCountryName)
    }

    private val differCallBack = object : DiffUtil.ItemCallback<University>() {
        override fun areItemsTheSame(oldItem: University, newItem: University): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: University, newItem: University): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.university_item, parent, false)
        return UniversityViewHolder(view)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.uniName.text = item.name
        holder.uniWebsite.text = item.webPages[0]
        holder.uniCountry.text = item.country
        holder.uniWebsite.setOnClickListener {
            listener.onClickWebsite(item.webPages[0])
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}

interface OnItemsClick {
    fun onClickWebsite(link: String)
}