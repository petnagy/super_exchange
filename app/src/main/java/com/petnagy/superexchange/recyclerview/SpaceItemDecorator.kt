package com.petnagy.superexchange.recyclerview

import android.graphics.Rect
import android.support.v7.widget.RecyclerView

/***
 * RecyclerView's item will be decorated with with spaces.
 */
class SpaceItemDecorator(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: android.view.View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = space
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
    }
}