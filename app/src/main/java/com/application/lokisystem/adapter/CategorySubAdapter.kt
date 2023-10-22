package com.application.lokisystem.adapter

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.CompanyDetail
import com.application.lokisystem.R
import com.application.lokisystem.databinding.RecyclerThemeBinding
import com.application.lokisystem.response.DataList
import com.bumptech.glide.Glide

class CategorySubAdapter: ListAdapter<DataList, CategorySubAdapter.DataListViewHolder>(DataListCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListViewHolder {
        val binding =
            RecyclerThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DataListViewHolder(private val binding: RecyclerThemeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataList) {
            with(binding) {
                textViewCompany.text = data.company.toString()
                textViewCode.findViewById<TextView>(R.id.textView_code).movementMethod =
                    LinkMovementMethod.getInstance()
                textViewSupply.text = data.supply.toString()

                if (data.imgSrc!!.isNotEmpty()) {
                    imageViewInitial.isVisible = true
                    textViewInitial.isVisible = true
                    Glide.with(itemView)
                        .load(data.imgSrc)
                        .centerCrop()
                        .into(imageViewInitial)
                } else {
                    textViewInitial.text = data.initial
                    val drawable = GradientDrawable()
                    drawable.cornerRadius = 250f //コーナーの設定方法
                    data.colorCode?.let { drawable.setColor(it.toColorInt()) } //背景色の変更方法1
                    textViewInitial.background = drawable
                }

                if (data.tradingHalt == 1) {
                    imageTradingHalt.visibility = View.VISIBLE
                    textViewCode.visibility = View.VISIBLE
                }
                rcTheme.setOnClickListener {
                    val intent = Intent(textViewCode.context, CompanyDetail::class.java)
                    intent.putExtra("code", data.code)
                    intent.putExtra("navigationName", binding.root.resources.getString(R.string.category))
                    textViewCode.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private val DataListCallback = object : DiffUtil.ItemCallback<DataList>() {
            override fun areItemsTheSame(oldItem: DataList, newItem: DataList): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: DataList, newItem: DataList): Boolean {
                return oldItem == newItem
            }
        }
    }
}