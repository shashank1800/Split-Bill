package com.shashankbhat.splitbill

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.shashankbhat.splitbill.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    lateinit var onBackPressed: OnBackPressedCallback
    lateinit var backEnabled : ObservableBoolean
    var title  = ObservableField("Split Bill")
    private var previousTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        backEnabled = ObservableBoolean(false)
        binding.isBackEnabled = backEnabled
        binding.title = title
        navController = Navigation.findNavController(this, R.id.fragment_container)
        navController.setGraph(R.navigation.navigations)

        onBackPressed = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (navController.currentDestination?.id) {
                        R.id.nav_home_screen -> finish()
                        else -> {
                            title.set(previousTitle)
                            navController.navigateUp()
                        }
                    }

                }
            }
        this.onBackPressedDispatcher.addCallback(this, onBackPressed)
        visibilityNavElements()

        uiToolbarBackClickListener()
    }

    private fun visibilityNavElements() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            previousTitle = title.get() ?: ""
            hideKeyboard()
            when (destination.id) {
                R.id.nav_home_screen -> backEnabled.set(false)
                R.id.nav_splash_screen -> backEnabled.set(false)

                else -> {
                    backEnabled.set(true)
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

    private fun uiToolbarBackClickListener(){
        binding.appBar.back().setOnClickListener {
            onBackPressed.handleOnBackPressed()
        }
    }

}

