package com.application.lokisystem.adapter

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.CompanyDetail
import com.application.lokisystem.R
import com.application.lokisystem.databinding.RecyclerListBinding
import com.application.lokisystem.response.Top5
import com.bumptech.glide.Glide

/**
 * ListAdapter<A: Object, B: RecyclerView.ViewHolder>(C: DiffUtils.ItemCallback<A>) を継承する
 * 今回は Top5 を表示するので、Top5 を表示するための View を保持した ViewHolder と User の差分を比較するための ItemCallback をセットしてやります。
 *
 * - A:Object には表示するデータを保持するクラスをセットする
 * - B:RecyclerView.ViewHolder には表示する View を保持する ViewHolder をセットする
 * - C:DiffUtils.ItemCallback<A> には A:Object の差分確認方法を実装した ItemCallback をセットする
 */
class SearchTop5: ListAdapter<Top5, SearchTop5.SupplyViewHolder>(SupplyCallback) {
    // ここで View を生成する、生成した View は ViewHolder に格納して、戻り値として返す
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplyViewHolder {
        val binding = RecyclerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupplyViewHolder(binding)
    }
    // その位置の Top5 を取得し、ViewHolder を通じて View に Top5 情報をセットする
    override fun onBindViewHolder(holder: SupplyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    /**
     * 生成した View を保持する、 bind で Top5 を　View に反映させる
     */
    class SupplyViewHolder(private val binding: RecyclerListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Top5) {
            with(binding) {
                textViewCompany.text = item.company
                textViewCode.movementMethod = LinkMovementMethod.getInstance()
                textViewSupply.text = item.supply.toString()
                if (item.imgSrc!!.isNotEmpty()) {
                    imageViewInitial.isVisible = true
                    textViewInitial.isVisible = true
                    Glide.with(itemView)
                        .load(item.imgSrc)
                        .centerCrop()
                        .into(imageViewInitial)
                } else {
                    textViewInitial.text = item.initial
                    val drawable = GradientDrawable()
                    drawable.cornerRadius = 250f
                    item.colorCode?.let { drawable.setColor(it.toColorInt()) }
                    textViewInitial.background = drawable
                }

                if (item.tradingHalt == 1) {
                    imageTradingHalt.visibility = View.VISIBLE
                    textViewCode.visibility = View.VISIBLE
                }
                rcList.setOnClickListener{
                    val intent = Intent(textViewCode.context, CompanyDetail::class.java)
                    intent.putExtra("code", item.code)
                    intent.putExtra("navigationName", root.resources.getString(R.string.search))
                    textViewCode.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        //Top5 差分確認する
        private val SupplyCallback = object : DiffUtil.ItemCallback<Top5>() {
            // 渡されたデータが同じ値であるか確認をする
            override fun areItemsTheSame(oldItem: Top5, newItem: Top5): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
            // 渡されたデータが同じ項目であるか確認する
            override fun areContentsTheSame(oldItem: Top5, newItem: Top5): Boolean {
                return oldItem == newItem
            }
        }
    }
}