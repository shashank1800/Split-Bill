package com.shashankbhat.splitbill.repository.remote.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.shashankbhat.splitbill.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.dto.bill_shares.BillSharesModel
import com.shashankbhat.splitbill.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.repository.local.BillRepository
import com.shashankbhat.splitbill.repository.local.BillShareRepository
import com.shashankbhat.splitbill.repository.remote.entity.BillSaveDto
import com.shashankbhat.splitbill.room_db.entity.Bill
import com.shashankbhat.splitbill.room_db.entity.BillShare
import com.shashankbhat.splitbill.room_db.entity.User
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.saveBill
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import javax.inject.Inject

class BillRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val billRepository: BillRepository,
    private val billShareRepository: BillShareRepository
) {

//    suspend fun getAllBill(groupId: Int) {
//        try {
//            val response = httpClient.post<User>(ApiConstants.BASE_URL + ApiConstants.saveUser) {
//                contentType(ContentType.Application.Json)
//                body = user ?: {}
//            }
//
//            userRepository.insert(response)
//        }catch (ex:Exception){
//
//        }
//    }
//
//    fun getAllBill(groupId: Int = 0) {
//        if (groupId != 0)
//            this.groupId = groupId
//
//        viewModelScope.launch {
//
//            val bills = arrayListOf<BillModel>()
//
//            val allBill = async { billRepo.getAllBill(this@BillShareViewModel.groupId) }
//            val allUsersByGroup =
//                async { userRepo.getAllUserByGroupId(this@BillShareViewModel.groupId) }
//            val userToIdMap = hashMapOf<Int, User>()
//
//            allUsersByGroup.await()?.forEach { user ->
//                userToIdMap[user.id?: -1] = user
//            }
//
//            allBill.await().forEach { bill ->
//                val billModel = BillModel(
//                    bill.id,
//                    bill.groupId,
//                    bill.name,
//                    bill.totalAmount,
//                    bill.dateCreated,
//                    null
//                )
//                bills.add(billModel)
//
//                val allBillShares = async { billShareRepo.getBillShareByBillId(bill.id) }
//                val billShares = arrayListOf<BillSharesModel>()
//
//                allBillShares.await().forEachIndexed { billShareIndex, billShare ->
//                    billShares.add(
//                        BillSharesModel(
//                            billShare.id,
//                            billShare.billId,
//                            billShare.userId,
//                            billShare.spent,
//                            billShare.share,
//                            billShare.dateCreated,
//                            userToIdMap.get(billShare.userId)
//                        )
//                    )
//                }
//
//                billModel.billShares = billShares
//            }
//
//            billList.value = bills
//        }
//    }

    suspend fun addBill(bill: Bill, billShareList: List<BillShareModel>) {
        val billShares = arrayListOf<BillShare>()

        billShareList.forEach {
            billShares.add(BillShare(userId = it.userId, share = it.share.value.toFloat(), spent = it.spent.value.toFloat()))
        }

        val response = httpClient.post<BillSaveDto>(BASE_URL + saveBill) {
            contentType(ContentType.Application.Json)
            body = BillSaveDto(bill, billShares)
        }

        Log.i("response", "$response")

        billRepository.insert(response.bill)
        response.billShares?.forEach {
            billShareRepository.insert(it)
        }

        try {


        }catch (ex:Exception){

        }
    }

//    fun deleteBill(billModel: BillModel) {
//        viewModelScope.launch {
//
//            billModel.billShares?.forEach { billSharesModel ->
//                val billShare = BillShare(
//                    billSharesModel.billId ?: 0,
//                    billSharesModel.userId ?: 0,
//                    billSharesModel.spent ?: 0F,
//                    billSharesModel.share ?: 0F
//                )
//                billShare.id = billSharesModel.id ?: 0
//                billShare.dateCreated = billSharesModel.dateCreated ?: 0
//                billShareRepo.delete(billShare)
//            }
//
//            val bill = Bill(billModel.id ?: 0, billModel.name ?: "", billModel.totalAmount ?: 0F)
//            bill.id = billModel.id ?: 0
//            bill.dateCreated = billModel.dateCreated ?: 0
//            val deleteCount = billRepo.delete(bill)
//
//            withContext(Dispatchers.IO) {
//                getAllBill()
//            }
//        }
//    }
}