package com.petnagy.superexchange.recyclerview

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/***
 * ViewHolder for databinding.
 */
class BindingViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun getBinding(): ViewDataBinding {
        return binding
    }
}
