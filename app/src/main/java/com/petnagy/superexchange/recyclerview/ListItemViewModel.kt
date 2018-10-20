package com.petnagy.superexchange.recyclerview

import android.databinding.BaseObservable

/***
 * ViewModel for databinding list items.
 */
abstract class ListItemViewModel : BaseObservable() {

    abstract fun getViewType(): Int

    abstract fun areItemsTheSame(newItem: ListItemViewModel): Boolean

    abstract fun areContentsTheSame(newItem: ListItemViewModel): Boolean
}