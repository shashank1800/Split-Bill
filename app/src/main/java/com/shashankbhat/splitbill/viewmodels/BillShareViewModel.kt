package com.shashankbhat.splitbill.viewmodels

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.database.remote.repository.BillRepositoryRemote
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.alogrithm.BillSplitAlgorithm
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
    var billListBalance: MutableLiveData<Response<List<BillModel>>> = MutableLiveData(Response.nothing())

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            billRepositoryRemote.getAllBill(this@BillShareViewModel.groupId, billList)
            withContext(Dispatchers.IO) {
                billList.value?.let {
                    when {
                        it.isSuccess() || (it.isLoading() || (it.data?.size ?: 0) > 0) -> {
                            isBillListEmpty.set((billList.value?.data?.size ?: 0) == 0)
                            billListBalance.postValue(Response.success(billList.value?.data))
                            billSplitAlgorithm = BillSplitAlgorithm(billList.value?.data ?: emptyList())
                        }
                    }
                }
            }
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

    var billSplitAlgorithm = BillSplitAlgorithm(emptyList())

    fun clear() {
        groupId = 0
        billList = MutableLiveData(Response.nothing())
        billListBalance = MutableLiveData(Response.nothing())
        isBillListEmpty.set(false)
    }

}