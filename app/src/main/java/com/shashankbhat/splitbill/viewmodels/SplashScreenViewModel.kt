package com.shashankbhat.splitbill.viewmodels

import androidx.lifecycle.ViewModel
import com.shashankbhat.splitbill.repository.remote.repository.BillRepositoryRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val billRepositoryRemote: BillRepositoryRemote,
) : ViewModel() {



}