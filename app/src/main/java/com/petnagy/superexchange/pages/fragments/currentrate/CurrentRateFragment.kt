package com.petnagy.superexchange.pages.fragments.currentrate

import android.Manifest
import android.arch.lifecycle.Observer
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
import com.petnagy.superexchange.permission.PermissionManager
import com.petnagy.superexchange.permission.PermissionStatus
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/***
 * Current Rate's Fragment.
 */
class CurrentRateFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: CurrentRateViewModelFactory

    @Inject
    lateinit var permissionManager: PermissionManager

    private lateinit var viewModel: CurrentRateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CurrentRateViewModel::class.java]
        lifecycle.addObserver(viewModel)
        viewModel.fineLocationPermissionStatus.observe(this, Observer<PermissionStatus> { permissionStatus ->
            Timber.d("PermissionStatus $permissionStatus")
            when (permissionStatus) {
                PermissionStatus.CAN_ASK_PERMISSION -> showPermissionNeedDialog()
                PermissionStatus.PERMISSION_DENIED -> askPermission()
                else -> {
                    //Do nothing
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    private fun checkPermission() {
        activity?.let { activity ->
            viewModel.fineLocationPermissionStatus.value = permissionManager.getPermissionStatus(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("CurrentRateFragment's view created")
        val binding: CurrentRateFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.current_rate_fragment, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return view
    }

    private fun askPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FINE_LOCATION)
    }

    private fun showPermissionNeedDialog() {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_CODE_FINE_LOCATION -> checkPermission()
        }
    }

    companion object {

        private const val REQUEST_CODE_FINE_LOCATION = 23123

        fun newInstance(): Fragment {
            return CurrentRateFragment()
        }

    }
}