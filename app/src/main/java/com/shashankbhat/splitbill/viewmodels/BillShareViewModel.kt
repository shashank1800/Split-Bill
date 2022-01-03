package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.model.BillListDto
import com.shashankbhat.splitbill.model.BillShareModel
import com.shashankbhat.splitbill.repository.local.BillRepository
import com.shashankbhat.splitbill.repository.local.BillShareRepository
import com.shashankbhat.splitbill.room_db.entity.Bill
import com.shashankbhat.splitbill.room_db.entity.BillShare
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

    var billListState = mutableStateOf(arrayListOf<List<BillListDto>?>())

    var groupId = 0

    fun getAllGroups(groupId: Int = 0) {
        if(groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {
            val result = billShareRepo.getAllBills(this@BillShareViewModel.groupId)
            withContext(Dispatchers.Main){

                val res = result?.groupBy { model->
                    model.billDetails?.bill_id
                }
                val billListStateTemp = arrayListOf<List<BillListDto>?>()
                res?.keys?.sortedBy { t -> t }?.forEach { id -> billListStateTemp.add(res[id])}

                billListState.value = billListStateTemp
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

            withContext(Dispatchers.IO){
                getAllGroups()
            }
        }
    }


}