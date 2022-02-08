package com.shashankbhat.splitbill.repository.remote.repository

import android.util.Log
import androidx.compose.runtime.MutableState
import com.shashankbhat.splitbill.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.dto.bill_shares.BillSharesModel
import com.shashankbhat.splitbill.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.repository.local.BillRepository
import com.shashankbhat.splitbill.repository.local.BillShareRepository
import com.shashankbhat.splitbill.repository.local.UserRepository
import com.shashankbhat.splitbill.repository.remote.entity.BillSaveDto
import com.shashankbhat.splitbill.repository.remote.entity.BillSharesGetAllDto
import com.shashankbhat.splitbill.room_db.entity.Bill
import com.shashankbhat.splitbill.room_db.entity.BillShare
import com.shashankbhat.splitbill.room_db.entity.User
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.BASE_URL
import com.shashankbhat.splitbill.ui.ApiConstants.saveBill
import com.shashankbhat.splitbill.ui.ApiConstants.getAllBill
import com.shashankbhat.splitbill.ui.ApiConstants.deleteBill
import com.shashankbhat.splitbill.util.Response
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class BillRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val billRepository: BillRepository,
    private val billShareRepository: BillShareRepository,
    private val userRepository: UserRepository
) {
    companion object{
        const val token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0NDE3NDE2MCwiaWF0IjoxNjQ0MTU2MTYwfQ.vvdL8WXXjzFC-CG-4rjaZkJ-CEZAxWPzseQ5SRBFe02O34By3BX0bq8GphJGSJvtd36HXXxSZ79zUY_-1qxzyQ"
    }

    suspend fun getAllBill(groupId: Int, billList: MutableState<Response<List<BillModel>>>) {

        try {
            getAllBillOffline(groupId, billList)

            val response = httpClient.get<BillSharesGetAllDto>(BASE_URL + getAllBill) {
                header(ApiConstants.AUTHORIZATION, token)
                parameter("groupId", groupId)
            }

            billList.value = Response.success(response.data)
        } catch (ex: Exception) {

        }
    }

    private suspend fun getAllBillOffline(
        groupId: Int,
        billList: MutableState<Response<List<BillModel>>>
    ) {
        val bills = arrayListOf<BillModel>()

        val allBill = billRepository.getAllBill(groupId)
        val allUsersByGroup = userRepository.getAllUsersByGroupId(groupId)
        val userToIdMap = hashMapOf<Int, User>()

        allUsersByGroup?.forEach { user ->
            userToIdMap[user.id ?: -1] = user
        }

        allBill.forEach { bill ->
            val billModel = BillModel(
                bill.id,
                bill.groupId,
                bill.name,
                bill.totalAmount,
                bill.dateCreated,
                null
            )
            bills.add(billModel)

            val allBillShares = billShareRepository.getBillShareByBillId(bill.id ?: -1)
            val billShares = arrayListOf<BillSharesModel>()

            allBillShares.forEachIndexed { billShareIndex, billShare ->
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
        billList.value = Response.loading(bills)
    }


    suspend fun addBill(bill: Bill, billShareList: List<BillShareModel>) {
        try {
            val billShares = arrayListOf<BillShare>()

            billShareList.forEach {
                billShares.add(
                    BillShare(
                        userId = it.userId,
                        share = it.share.value.toFloat(),
                        spent = it.spent.value.toFloat()
                    )
                )
            }

            val response = httpClient.post<BillSaveDto>(BASE_URL + saveBill) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, token)
                body = BillSaveDto(bill, billShares)
            }

            Log.i("response", "$response")

            billRepository.insert(response.bill)
            response.billShares?.forEach {
                billShareRepository.insert(it)
            }

        } catch (ex: Exception) {

        }
    }


    suspend fun deleteBill(billModel: BillModel) {

        val response = httpClient.put<BillModel>(BASE_URL + deleteBill) {
            contentType(ContentType.Application.Json)
            header(ApiConstants.AUTHORIZATION, token)
            body = billModel
        }

        Log.i("response", "$response")

        if(response != null)
            deleteBillOffline(billModel)
        try {

        } catch (ex: Exception) {

        }
    }

    private suspend fun deleteBillOffline(billModel: BillModel) {
        billModel.billShares?.forEach { billSharesModel ->
            val billShare = BillShare(
                billSharesModel.billId ?: 0,
                billSharesModel.userId ?: 0,
                billSharesModel.spent ?: 0F,
                billSharesModel.share ?: 0F
            )
            billShare.id = billSharesModel.id ?: 0
            billShare.dateCreated = billSharesModel.dateCreated ?: 0
            billShareRepository.delete(billShare)
        }

        val bill = Bill(billModel.id ?: 0, billModel.name ?: "", billModel.totalAmount ?: 0F)
        bill.id = billModel.id ?: 0
        bill.dateCreated = billModel.dateCreated ?: 0
        val deleteCount = billRepository.delete(bill)

    }
}