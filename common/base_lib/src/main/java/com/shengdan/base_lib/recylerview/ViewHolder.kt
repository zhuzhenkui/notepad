package com.shengdan.base_lib.recylerview

import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder

class ViewHolder(itemView: ViewDataBinding) : BaseViewHolder(itemView.root) {
    var binding: ViewDataBinding? = null

    init {
        //这里执行kotlin主构造方法
        binding = itemView
    }
}