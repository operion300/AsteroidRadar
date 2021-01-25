package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemBinding
import com.udacity.asteroidradar.domain.Asteroid

//recyclerview adapter
class AsteroidAdapter(private val clickListener:ItemListener):ListAdapter<Asteroid,AsteroidAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    class ItemViewHolder private constructor(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){

        //binding to xml
        fun bind(item:Asteroid, clickListener:ItemListener){
            binding.asteroidXml = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        //inflater method
        companion object{
            fun from(parent:ViewGroup):ItemViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(inflater,parent,false)

                return ItemViewHolder(binding)
            }
        }

    }

    //callback to listen item changes
    class DiffCallback: DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    //class to handle item click
    class ItemListener(val clickListener: (asteroid:Asteroid)-> Unit ){
        fun onItemClick(asteroid:Asteroid) = clickListener(asteroid)
    }
}