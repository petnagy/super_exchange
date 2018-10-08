package com.petnagy.superexchange.databinding

import android.databinding.BindingAdapter
import android.widget.ArrayAdapter
import android.widget.Spinner

@BindingAdapter("data")
fun setupSpinnerData(spinner: Spinner, list: List<String>) {
    val adapter = ArrayAdapter<String>(spinner.context, android.R.layout.simple_spinner_dropdown_item, list)
    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter
}
