package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.database.remote.repository.BillRepositoryRemote
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.database.remote.repository.UserRepositoryRemote
import com.shashankbhat.splitbill.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class BillShareViewModel @Inject constructor(
    private val billRepositoryRemote: BillRepositoryRemote,
    val sharedPreferences: SharedPreferences
) : ViewModel() {

    var groupId = 0
    var billList: MutableLiveData<Response<List<BillModel>>> = MutableLiveData(Response.nothing())
    var isBillListEmpty = ObservableBoolean(false)

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            billRepositoryRemote.getAllBill(this@BillShareViewModel.groupId, billList)
            withContext(Dispatchers.IO) {
                when {
                    billList.value?.isSuccess() ?: false -> {
                        isBillListEmpty.set((billList.value?.data?.size ?: 0) == 0)
                    }
                }

            }
        }
    }

    var billListBalance: MutableLiveData<Response<List<BillModel>>> = MutableLiveData(Response.nothing())

    fun getAllBillForShowingBalances(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            billRepositoryRemote.getAllBill(this@BillShareViewModel.groupId, billListBalance)
        }
    }


    fun addBill(bill: Bill, billShareList: List<BillShareModel>) {
        GlobalScope.launch {
            billRepositoryRemote.addBill(bill, billShareList){ type ->
                when{
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

    fun clear() {
        groupId = 0
        billList = MutableLiveData(Response.nothing())
        billListBalance = MutableLiveData(Response.nothing())
        isBillListEmpty.set(false)
    }

//    fun deleteBill(billModel: BillModel) {
//        GlobalScope.launch {
//            billRepositoryRemote.deleteBill(billModel){ type ->
//                when {
//                    type.isLocal() -> viewModelScope.launch {
//                        billRepositoryRemote.getAllBillOffline(this@BillShareViewModel.groupId, billList)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getAllUsersByGroupId() {
//        viewModelScope.launch {
//            userRepoRemote.getAllUsersByGroupId(this@BillShareViewModel.groupId, MutableLiveData<Response<List<User>>>(Response.nothing()))
//        }
//    }


}