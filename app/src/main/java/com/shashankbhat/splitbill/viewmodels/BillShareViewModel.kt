package com.shashankbhat.splitbill.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.dto.bill_shares.BillSharesModel
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

            allUsersByGroup.await()?.forEach { user ->
                userToIdMap[user.id?: -1] = user
            }

            allBill.await().forEach { bill ->
                val billModel = BillModel(
                    bill.id,
                    bill.groupId,
                    bill.name,
                    bill.totalAmount,
                    bill.dateCreated,
                    null
                )
                bills.add(billModel)

                val allBillShares = async { billShareRepo.getBillShareByBillId(bill.id) }
                val billShares = arrayListOf<BillSharesModel>()

                allBillShares.await().forEachIndexed { billShareIndex, billShare ->
                    billShares.add(
                        BillSharesModel(
                            billShare.id,
                            billShare.billId,
                            billShare.userId,
                            billShare.spent,
                            billShare.share,
                            billShare.dateCreated,
                            userToIdMap.get(billShare.userId)
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
                        it.userId,
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

    fun deleteBill(billModel: BillModel) {
        viewModelScope.launch {

            billModel.billShares?.forEach { billSharesModel ->
                val billShare = BillShare(
                    billSharesModel.billId ?: 0,
                    billSharesModel.userId ?: 0,
                    billSharesModel.spent ?: 0F,
                    billSharesModel.share ?: 0F
                )
                billShare.id = billSharesModel.id ?: 0
                billShare.dateCreated = billSharesModel.dateCreated ?: 0
                billShareRepo.delete(billShare)
            }

            val bill = Bill(billModel.id ?: 0, billModel.name ?: "", billModel.totalAmount ?: 0F)
            bill.id = billModel.id ?: 0
            bill.dateCreated = billModel.dateCreated ?: 0
            val deleteCount = billRepo.delete(bill)

            withContext(Dispatchers.IO) {
                getAllBill()
            }
        }
    }


}