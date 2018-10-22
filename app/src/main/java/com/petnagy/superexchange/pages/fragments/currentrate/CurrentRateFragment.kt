package com.petnagy.superexchange.pages.fragments.currentrate

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petnagy.superexchange.R
import com.petnagy.superexchange.databinding.CurrentRateFragmentBinding
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModel
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateViewModelFactory
import com.petnagy.superexchange.permission.PermissionManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.location_permission_need_dialog.view.*
import timber.log.Timber
import javax.inject.Inject
import android.support.annotation.StringRes

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
        viewModel.status.observe(this, Observer<LocationStatus> { locationStatus ->
            Timber.d("Status changed: ${locationStatus?.name}")
            when (locationStatus) {
                LocationStatus.PERMISSION_DENIED -> askPermission()
                LocationStatus.PERMISSION_NEED -> showPermissionNeedDialog()
                LocationStatus.LOCATION_ERROR -> showLocationErrorDialog()
                LocationStatus.PLAY_SERVICE_ERROR -> showPlayServiceErrorDialog()
                LocationStatus.SETTING_ERROR -> showSettingsErrorDialog()
                LocationStatus.NETWORK_ERROR -> showNetworkErrorDialog()
                LocationStatus.NOT_VALID_COUNTRY_CODE -> showNotValidCountryCodeDialog()
                else -> {
                    // DO nothing
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
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val view = layoutInflater.inflate(R.layout.location_permission_need_dialog, null)
            builder.setView(view)
                    .setNegativeButton(R.string.dialog_cancel_button) { dialog, _ ->
                        dialog.cancel()
                    }
            val dialog = builder.show()
            view.btn_settings_launch.setOnClickListener { _ ->
                launchAskPermission()
                dialog.dismiss()
            }
        }
    }

    private fun launchAskPermission() {
        val intent = Intent()
        intent.action = ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = fromParts("package", activity?.packageName, null)
        startActivity(intent)
    }

    private fun showPlayServiceErrorDialog() {
        createSimpleAlertDialog(R.string.dialog_no_play_service_title, R.string.dialog_no_play_service_message)
    }

    private fun showSettingsErrorDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.dialog_setting_error_title)
                    .setMessage(R.string.dialog_setting_error_message)
                    .setNegativeButton(R.string.dialog_cancel_button) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(R.string.dialog_ok_button) { dialog, _ ->
                        launchLocationSettings()
                        dialog.cancel()
                    }
        }
    }

    private fun launchLocationSettings() {
        val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(viewIntent)
    }

    private fun showLocationErrorDialog() {
        createSimpleAlertDialog(R.string.dialog_location_error_title, R.string.dialog_location_error_message)
    }

    private fun showNetworkErrorDialog() {
        createSimpleAlertDialog(R.string.dialog_network_error_title, R.string.dialog_network_error_message)
    }

    private fun showNotValidCountryCodeDialog() {
        createSimpleAlertDialog(R.string.dialog_no_valid_country_code_title, R.string.dialog_no_valid_country_code_message)
    }

    private fun createSimpleAlertDialog(@StringRes titleResId: Int, @StringRes messageResId: Int) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(titleResId)
                    .setMessage(messageResId)
                    .setNegativeButton(R.string.dialog_ok_button) { dialog, _ ->
                        dialog.cancel()
                    }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_FINE_LOCATION -> checkPermission()
        }
        viewModel.start()
    }

    companion object {

        private const val REQUEST_CODE_FINE_LOCATION = 23123

        fun newInstance(): Fragment {
            return CurrentRateFragment()
        }
    }
}
