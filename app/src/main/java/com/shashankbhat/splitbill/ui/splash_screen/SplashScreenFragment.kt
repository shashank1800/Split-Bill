package com.shashankbhat.splitbill.ui.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.viewmodels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.extension.getToken

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var navController: NavController

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()

        if(viewModel.sharedPreferences.getToken().isNullOrEmpty()){
            viewModel.login()
        }else{
            navController.navigate(R.id.nav_home_screen)
        }
        viewModel.loginState.observe(viewLifecycleOwner) { tokenData ->
            if(tokenData.status == Status.Success){
                navController.navigate(R.id.nav_home_screen)
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    BillShares()
                }
            }
        }
    }


    @ExperimentalMaterialApi
    @Composable
    fun BillShares() {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {

            }

        }
    }


    @ExperimentalMaterialApi
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SplitBillTheme {
            BillShares()
        }
    }
}