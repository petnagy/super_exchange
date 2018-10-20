package com.petnagy.superexchange.databinding

import android.databinding.BindingAdapter
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.petnagy.superexchange.recyclerview.ListItemViewModel
import com.petnagy.superexchange.recyclerview.RecyclerViewAdapter
import com.petnagy.superexchange.recyclerview.SpaceItemDecorator

/***
 * RecyclerView realted binding adapters, to use it in view.
 */
@BindingAdapter("source")
fun setupRecyclerViewSource(recyclerView: RecyclerView, source: List<ListItemViewModel>) {
    getAdapter(recyclerView).items = source.toMutableList()
}

@BindingAdapter("itemLayout")
fun setupRecyclerViewItemLayout(recyclerView: RecyclerView, @LayoutRes itemLayout: Int) {
    getAdapter(recyclerView).itemLayout = itemLayout
}

private fun getAdapter(recyclerView: RecyclerView): RecyclerViewAdapter {
    var adapter: RecyclerViewAdapter? = recyclerView.adapter as? RecyclerViewAdapter
    if (adapter == null) {
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, 1)
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
        val space = 8
        recyclerView.addItemDecoration(SpaceItemDecorator(space))
    }
    return adapter
}
