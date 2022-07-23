package com.shashankbhat.splitbill.ui.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shashankbhat.splitbill.viewmodels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.databinding.FragmentSplashScreenBinding
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.extension.getToken

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val token = viewModel.sharedPreferences.getToken();
        if(token.isEmpty() || token.trim().compareTo("Bearer") == 0){
            viewModel.login()
        }else{
            navController.navigate(R.id.nav_home_screen)
        }
        viewModel.loginState.observe(viewLifecycleOwner) { tokenData ->
            if(tokenData.status == Status.Success){
                navController.navigate(R.id.nav_home_screen)
            }
        }


    }

    companion object {

        @JvmStatic
        fun newInstance() = SplashScreenFragment()
    }
}