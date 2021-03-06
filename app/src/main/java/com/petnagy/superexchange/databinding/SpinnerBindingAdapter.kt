package com.petnagy.superexchange.databinding

import android.databinding.BindingAdapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

/***
 * Spinner related binding adapter.
 */

@BindingAdapter("data")
fun setupSpinnerData(spinner: Spinner, list: List<String>) {
    val adapter = ArrayAdapter<String>(spinner.context, android.R.layout.simple_spinner_dropdown_item, list)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter
}

@BindingAdapter("selectedValue")
fun setupSelectedValue(spinner: Spinner, value: String?) {
    if (spinner.adapter.count > 0) {
        val selectedIndex = IntRange(0, spinner.count).firstOrNull { item -> value != null && spinner.getItemAtPosition(item) == value }
        selectedIndex?.let { index ->
            spinner.setSelection(index)
        }
    }
}

@BindingAdapter("selectedListener")
fun setupSelectionListener(spinner: Spinner, onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    spinner.onItemSelectedListener = onItemSelectedListener
}

@BindingAdapter("enabled")
fun setupEnabled(spinner: Spinner, value: Boolean) {
    spinner.isEnabled = value
}
