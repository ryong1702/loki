package com.application.lokisystem.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.CompanyDetail
import com.application.lokisystem.R
import com.application.lokisystem.databinding.RecyclerSearchListBinding
import com.application.lokisystem.response.Candidates

class SearchAdapter: ListAdapter<Candidates, SearchAdapter.CandidatesViewHolder>(CandidatesCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatesViewHolder {
        val binding =
            RecyclerSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidatesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CandidatesViewHolder(private val binding: RecyclerSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(candidate: Candidates) {
            with(binding) {

                textViewCodeName.text = candidate.name.toString().plus("(").plus(candidate.code.toString()).plus(")")
                root.setOnClickListener {
                    val intent = Intent(root.context, CompanyDetail::class.java)
                    intent.putExtra("code", candidate.code)
                    intent.putExtra("navigationName", root.resources.getString(R.string.search))
                    root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private val CandidatesCallback = object : DiffUtil.ItemCallback<Candidates>() {
            override fun areItemsTheSame(oldItem: Candidates, newItem: Candidates): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Candidates, newItem: Candidates): Boolean {
                return oldItem == newItem
            }
        }
    }
}