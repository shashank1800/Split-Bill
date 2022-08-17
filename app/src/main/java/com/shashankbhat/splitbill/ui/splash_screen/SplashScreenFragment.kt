package com.shashankbhat.splitbill.ui.splash_screen

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shashankbhat.splitbill.viewmodels.SplashScreenViewModel
import com.shashankbhat.splitbill.database.remote.entity.TokenDto
import com.shashankbhat.splitbill.databinding.FragmentSplashScreenBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.util.NetworkCheck.isInternetAvailable
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.extension.getToken
import com.shashankbhat.splitbill.util.extension.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: FragmentSplashScreenBinding
    private var navController: NavController? = null
    private var waitTime = 1000L
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback : ConnectivityManager.NetworkCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        connectivityManager = requireActivity().getSystemService(ConnectivityManager::class.java) as ConnectivityManager

        /**
         * Check if network is available or not,
         * If available
         *      - Check server is alive or wait for the server to go live (Cause of heroku
         *      puts server off if not used for more than 30 min)
         *      - If already logged in and token is not empty navigate to home screen
         *      - Else register/login
         *
         * Else
         *     - If already logged in and token is not empty navigate to home screen
         *     - Else ask for the user to enable network
         */

        if(isInternetAvailable(requireContext()))
            onInternetAvailable()
        else
            onInternetUnavailable()

        listenToNetworkChanges()

        viewModel.loginState.observe(viewLifecycleOwner) { tokenData ->
            if(tokenData.status == Status.Success){
                connectivityManager.unregisterNetworkCallback(networkCallback)
                navController?.navigate(SplashScreenFragmentDirections
                    .actionNavSplashScreenToNavHomeScreen()
                )
                navController = null
            }
        }
    }

    private fun onInternetAvailable(){
        lifecycleScope.launch {
            // Wait for the server to come live
            while (!viewModel.ping()){
                delay(waitTime)
            }
            if (isLoggedIn())
                viewModel.loginState.value = Response.success(TokenDto())
            else
                viewModel.login()
        }
    }

    private fun onInternetUnavailable(){
        if (isLoggedIn())
            viewModel.loginState.value = Response.success(TokenDto())
        else
            binding.showSnackBar(
                "Please enable internet", "Okay",
                duration = Snackbar.LENGTH_INDEFINITE,
                snackBarType = SnackBarType.INSTRUCTION
            )
    }

    private fun listenToNetworkChanges() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
//                onInternetAvailable()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                onInternetUnavailable()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                onInternetUnavailable()
            }
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun isLoggedIn(): Boolean {
        val token = viewModel.sharedPreferences.getToken()
        return token.isNotEmpty() && token.trim().compareTo("Bearer") != 0
    }


}