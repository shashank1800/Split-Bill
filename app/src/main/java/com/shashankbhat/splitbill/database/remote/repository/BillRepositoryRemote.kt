package com.shashankbhat.splitbill.database.remote.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shashankbhat.splitbill.BuildConfig.BASE_URL
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillSharesModel
import com.shashankbhat.splitbill.database.local.model.bill_shares.BillShareModel
import com.shashankbhat.splitbill.database.local.repository.BillRepository
import com.shashankbhat.splitbill.database.local.repository.BillShareRepository
import com.shashankbhat.splitbill.database.local.repository.UserRepository
import com.shashankbhat.splitbill.database.remote.entity.BillSaveDto
import com.shashankbhat.splitbill.database.remote.entity.BillSharesGetAllDto
import com.shashankbhat.splitbill.database.local.entity.Bill
import com.shashankbhat.splitbill.database.local.entity.BillShare
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.ui.ApiConstants
import com.shashankbhat.splitbill.ui.ApiConstants.saveBill
import com.shashankbhat.splitbill.ui.ApiConstants.getAllBill
import com.shashankbhat.splitbill.ui.ApiConstants.deleteBill
import com.shashankbhat.splitbill.util.DatabaseOperation
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.getLocalId
import com.shashankbhat.splitbill.util.extension.getToken
import com.shashankbhat.splitbill.util.extension.getUniqueId
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class BillRepositoryRemote @Inject constructor(
    private val httpClient: HttpClient,
    private val billRepository: BillRepository,
    private val billShareRepository: BillShareRepository,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getAllBill(groupId: Int, billList: MutableLiveData<Response<List<BillModel>>>) {

        try {
            getAllBillOffline(groupId, billList)

            val response = httpClient.get<BillSharesGetAllDto>(BASE_URL + getAllBill) {
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                parameter("groupId", groupId)
            }

            if((billList.value?.data?.size ?: 0) > 0)
                checkAnyDataDeleted(billList.value?.data, response.data)

            billList.value = Response.success(response.data)

            response.data.forEach {
                billRepository.insert(
                    bill = Bill(
                        it.groupId ?: 0,
                        it.name ?: "",
                        it.totalAmount ?: 0F,
                        it.id,
                        it.dateCreated,
                        it.uniqueId
                    )
                )
                it.billShares?.forEach { billShare ->
                    billShareRepository.insert(billShare =
                        BillShare(
                            billShare.billId,
                            billShare.userId,
                            billShare.spent,
                            billShare.share,
                            billShare.id,
                            billShare.uniqueId,
                            billShare.dateCreated
                        )
                    )
                }
            }
        } catch (ex: Exception) {

        }
    }

    private suspend fun checkAnyDataDeleted(
        localList: List<BillModel>?,
        remoteList: List<BillModel>?
    ){
        val idSet = HashSet<Int>()

        if(localList != null && remoteList != null){
            remoteList.forEach {
                it.id?.let { it1 -> idSet.add(it1) }
            }

            localList.forEach {
                if(it.id != null || !idSet.contains(it.id)){
                    deleteBillOffline(it)
                }
            }
        }
    }

    suspend fun getAllBillOffline(
        groupId: Int,
        billList: MutableLiveData<Response<List<BillModel>>>
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
                bill.uniqueId
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
                        userToIdMap[billShare.userId]
                    )
                )
            }

            billShares.sortBy { billSharesModel -> billSharesModel.user?.dateCreated }

            billModel.billShares = billShares
        }
        billList.value = Response.success(bills)
    }


    suspend fun addBill(bill: Bill, billShareList: List<BillShareModel>, addLocalCallback: (type: DatabaseOperation) -> Unit) {
        try {
            val billShares = arrayListOf<BillShare>()
            val billIdLocal = sharedPreferences.getLocalId()
            bill.id = billIdLocal
            bill.uniqueId = sharedPreferences.getUniqueId()
            bill.dateCreated = System.currentTimeMillis()

            billShareList.forEach {
                billShares.add(
                    BillShare(
                        id = sharedPreferences.getLocalId(),
                        billId = billIdLocal,
                        userId = it.user.id,
                        share = it.share.get()?.toFloat(),
                        spent = it.spent.get()?.toFloat(),
                        dateCreated = System.currentTimeMillis()
                    )
                )
            }

            billRepository.insert(bill)
            billShares.forEach {
                billShareRepository.insert(it)
            }

            addLocalCallback(DatabaseOperation.LOCAL)

            val token = sharedPreferences.getToken()

            val response = httpClient.post<BillSaveDto>(BASE_URL + saveBill) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, token)
                body = BillSaveDto(bill, billShares)
            }

            Log.i("response", "$response")


            billRepository.update(billIdLocal, response.bill?.id ?: 0)
//            sharedPreferences.releaseOne()

            if(response.billShares?.size == billShares.size){
                billShares.forEachIndexed { index, it ->

                    val billShareIdLocal = it.id ?: 0
                    val billShareIdRemote = response.billShares?.get(index)?.id
                    billShareRepository.update(billShareIdLocal, response.bill?.id ?: 0, billShareIdRemote ?: 0)
//                    sharedPreferences.releaseOne()
                }
            }

            addLocalCallback(DatabaseOperation.REMOTE)

        } catch (ex: Exception) {
            print("ERROR MESSAGE $ex")
        }
    }


    suspend fun deleteBill(billModel: BillModel, databaseCallback: (type: DatabaseOperation) -> Unit) {

        try {
            deleteBillOffline(billModel)
            databaseCallback(DatabaseOperation.LOCAL)

            val response = httpClient.put<BillModel>(BASE_URL + deleteBill) {
                contentType(ContentType.Application.Json)
                header(ApiConstants.AUTHORIZATION, sharedPreferences.getToken())
                body = billModel
            }

            Log.i("response", "$response")
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