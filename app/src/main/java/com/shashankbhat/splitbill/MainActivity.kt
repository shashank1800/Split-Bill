package com.shashankbhat.splitbill

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.shashankbhat.splitbill.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.fragment_container)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (navController.currentDestination?.id) {
                        R.id.nav_home_screen -> finish()
                        else -> navController.navigateUp()
                    }

                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)
        visibilityNavElements()
    }

    private fun visibilityNavElements() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            hideKeyboard()
            when (destination.id) {
                R.id.nav_home_screen -> supportActionBar?.setDisplayHomeAsUpEnabled(false)
                R.id.nav_splash_screen -> supportActionBar?.setDisplayHomeAsUpEnabled(false)

                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                hideKeyboard()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            val windowToken = currentFocus?.windowToken
            hideSoftInputFromWindow(
                windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
            currentFocus?.clearFocus()
        }
    }

}

