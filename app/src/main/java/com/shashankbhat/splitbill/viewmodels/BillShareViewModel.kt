package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.model.BillModel
import com.shashankbhat.splitbill.model.BillShareModel
import com.shashankbhat.splitbill.model.BillSharesModel
import com.shashankbhat.splitbill.repository.local.BillRepository
import com.shashankbhat.splitbill.repository.local.BillShareRepository
import com.shashankbhat.splitbill.repository.local.UserRepository
import com.shashankbhat.splitbill.room_db.entity.Bill
import com.shashankbhat.splitbill.room_db.entity.BillShare
import com.shashankbhat.splitbill.room_db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class BillShareViewModel @Inject constructor(
    private val billRepo: BillRepository,
    private val billShareRepo: BillShareRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    var groupId = 0
    var billList = mutableStateOf(arrayListOf<BillModel>())

    fun getAllBill(groupId: Int = 0) {
        if (groupId != 0)
            this.groupId = groupId

        viewModelScope.launch {

            val bills = arrayListOf<BillModel>()

            val allBill = async { billRepo.getAllBill(this@BillShareViewModel.groupId) }
            val allUsersByGroup =
                async { userRepo.getAllUserByGroupId(this@BillShareViewModel.groupId) }
            val userToIdMap = hashMapOf<Int, User>()

            allUsersByGroup.await().forEach { user ->
                userToIdMap[user.id] = user
            }

            allBill.await().forEach { bill ->
                val billModel = BillModel(
                    bill.id,
                    bill.group_id,
                    bill.name,
                    bill.total_amount,
                    bill.date_created,
                    null
                )
                bills.add(billModel)

                val allBillShares = async { billShareRepo.getBillShareByBillId(bill.id) }
                val billShares = arrayListOf<BillSharesModel>()

                allBillShares.await().forEachIndexed { billShareIndex, billShare ->
                    billShares.add(
                        BillSharesModel(
                            billShare.id,
                            billShare.bill_id,
                            billShare.user_id,
                            billShare.spent,
                            billShare.share,
                            billShare.date_created,
                            userToIdMap.get(billShare.user_id)
                        )
                    )
                }

                billModel.billShares = billShares
            }

            billList.value = bills
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

            withContext(Dispatchers.IO) {
                getAllBill()
            }
        }
    }


}