package com.application.lokisystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.CustomDecoration
import com.application.lokisystem.R
import com.application.lokisystem.databinding.FragmentCategoryBinding
import com.application.lokisystem.response.Themes

class CategoryAdapter : ListAdapter<Themes, CategoryAdapter.ThemeViewHolder>(ThemeCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding =
            FragmentCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ThemeViewHolder(private val binding: FragmentCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val categorySubAdapter by lazy {
            CategorySubAdapter()
        }

        fun bind(theme: Themes) {
            with(binding) {
                binding.tvTheme.text = theme.theme.toString()
                val recyclerSubView: RecyclerView = binding.root.findViewById(R.id.rv_category)
                recyclerSubView.layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
                recyclerSubView.setHasFixedSize(false)
                recyclerSubView.adapter = categorySubAdapter
                recyclerSubView.addItemDecoration(CustomDecoration())
                categorySubAdapter.submitList(theme.data!!)
            }
        }
    }

    companion object {
        private val ThemeCallback = object : DiffUtil.ItemCallback<Themes>() {
            override fun areItemsTheSame(oldItem: Themes, newItem: Themes): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Themes, newItem: Themes): Boolean {
                return oldItem == newItem
            }
        }
    }
}