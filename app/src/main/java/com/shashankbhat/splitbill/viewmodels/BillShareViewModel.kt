package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.database.remote.repository.BillRepositoryRemote
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class BillShareViewModel @Inject constructor(
    private val billRepositoryRemote: BillRepositoryRemote
) : ViewModel() {

    var groupId = 0
    var billList: MutableState<Response<List<BillModel>>> = mutableStateOf(Response.isNothing())

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            billRepositoryRemote.getAllBill(this@BillShareViewModel.groupId, billList)
        }
    }


    fun addBill(bill: Bill, billShareList: List<BillShareModel>) {
        GlobalScope.launch {
            billRepositoryRemote.addBill(bill, billShareList){ type ->
                when(type.isLocal()){
                    type.isLocal() -> viewModelScope.launch {
                        billRepositoryRemote.getAllBillOffline(this@BillShareViewModel.groupId, billList)
                    }

                    type.isRemote() -> viewModelScope.launch {
                        billRepositoryRemote.getAllBill(this@BillShareViewModel.groupId, billList)
                    }
                }
            }
        }
    }

    fun deleteBill(billModel: BillModel) {
        GlobalScope.launch {
            billRepositoryRemote.deleteBill(billModel){ type ->
                when(type.isLocal()){
                    type.isLocal() -> viewModelScope.launch {
                        billRepositoryRemote.getAllBillOffline(this@BillShareViewModel.groupId, billList)
                    }
                }
            }
        }
    }


}