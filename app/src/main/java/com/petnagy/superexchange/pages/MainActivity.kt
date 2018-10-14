package com.petnagy.superexchange.pages

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.petnagy.koredux.Store
import com.petnagy.koredux.StoreSubscriber
import com.petnagy.superexchange.R
import com.petnagy.superexchange.pages.fragments.currentrate.CurrentRateFragment
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.redux.state.FragmentState
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/***
 * First and only Activity to contains all pages (fragments) in the application.
 */
class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, StoreSubscriber<AppState> {

    @Inject
    lateinit var store: Store<AppState>

    companion object {
        private const val CONTENT_TAG = "contentTag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDrawerAndToolbar()
        setupNavigationView()
        initFragment()
    }

    override fun onStart() {
        super.onStart()
        store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        store.unsubscribe(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.today_rate) {

        } else if (menuItem.itemId == R.id.history_rate) {

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupDrawerAndToolbar() {
        setSupportActionBar(toolbar)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun setupNavigationView() {
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initFragment() {
        var fragment = supportFragmentManager.findFragmentByTag(CONTENT_TAG)
        if (fragment == null) {
            fragment = CurrentRateFragment.newInstance()
        }
        replaceContent(fragment)
    }

    private fun replaceContent(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.base_content_frame, fragment, CONTENT_TAG).commit()
    }

    override fun newState(state: AppState) {
        if (state.fragmentState == FragmentState.LATEST_RATE) {
            val fragment = supportFragmentManager.findFragmentByTag(CONTENT_TAG)
            if (fragment !is CurrentRateFragment) {
                replaceContent(CurrentRateFragment.newInstance())
            }
        }
    }
}
