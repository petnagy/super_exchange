package com.petnagy.superexchange.pages.fragments.currentrate

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petnagy.superexchange.R
import com.petnagy.superexchange.databinding.CurrentRateFragmentBinding
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModel
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/***
 * Current Rate's Fragment.
 */
class CurrentRateFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: CurrentRateViewModelFactory

    private lateinit var viewModel: CurrentRateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CurrentRateViewModel::class.java]
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("CurrentRateFragment's view created")
        val binding: CurrentRateFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.current_rate_fragment, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return view
    }

    companion object {

        fun newInstance(): Fragment {
            return CurrentRateFragment()
        }

    }
}