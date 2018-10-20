package com.petnagy.superexchange.pages.fragments.history

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petnagy.superexchange.R
import com.petnagy.superexchange.databinding.HistoryLayoutBinding
import com.petnagy.superexchange.pages.fragments.history.viewmodel.HistoryViewModel
import com.petnagy.superexchange.pages.fragments.history.viewmodel.HistoryViewModelFactory
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class HistoryFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: HistoryViewModelFactory

    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("HistoryFragment's view created")
        val binding: HistoryLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.history_layout, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[HistoryViewModel::class.java]
        lifecycle.addObserver(viewModel)
    }

    companion object {
        fun newInstance(): Fragment {
            return HistoryFragment()
        }
    }
}