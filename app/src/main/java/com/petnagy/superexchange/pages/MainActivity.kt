package com.petnagy.superexchange.pages

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.petnagy.superexchange.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/***
 * First and only Activity to contains all pages (fragments) in the application.
 */
class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
            //fragment = XYFragment.newInstance()
        }
        //replaceContent(fragment)
    }

    private fun replaceContent(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.base_content_frame, fragment, CONTENT_TAG).commit()
    }
}
