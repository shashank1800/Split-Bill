package com.example.splitbill.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitbill.model.BillListModel
import com.example.splitbill.model.BillShareModel
import com.example.splitbill.repository.local.BillRepository
import com.example.splitbill.repository.local.BillShareRepository
import com.example.splitbill.room_db.entity.Bill
import com.example.splitbill.room_db.entity.BillShare
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BillShareViewModel @Inject constructor(
    private val billRepo: BillRepository,
    private val billShareRepo: BillShareRepository
) : ViewModel() {

    private var billList : List<BillListModel>? = null
    var billListState : MutableState<List<BillListModel>> = mutableStateOf(arrayListOf())

    fun getAllGroups(groupId: Int) {
        viewModelScope.launch {
            val result = billRepo.getAllBills(groupId)
            withContext(Dispatchers.Main){
                billList = result

                billListState.value = billList?: emptyList()
            }
        }
    }


    fun addBill(bill: Bill, billShareList: List<BillShareModel>) {
        viewModelScope.launch {
            val billIdDeferred = async {
                billRepo.insert(bill)
            }

            billShareList.forEach {
                val billId = billIdDeferred.await()
                billShareRepo.insert(
                    BillShare(
                        billId.toInt(),
                        it.user_id,
                        it.spent.value.toFloat(),
                        it.share.value.toFloat()
                    )
                )
            }
        }
    }


}