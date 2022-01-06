package com.shashankbhat.splitbill.util.extension

import com.shashankbhat.splitbill.model.BillModel
import com.shashankbhat.splitbill.room_db.entity.User
import kotlin.math.abs

class BillSplitAlgorithm(private val bills: ArrayList<BillModel>) {

    private var min = Int.MAX_VALUE
    private var minTransactions: List<BillShareBalance>? = null
    private var spentAndShares = arrayListOf<BillSpentAndShare>()

    init {
        splitBill()
    }

    private fun splitBillAlgorithm(
        sharesPositive: List<BillSpentAndShare>,
        sharesNegative: List<BillSpentAndShare>,
        transaction: ArrayList<BillShareBalance>
    ) {
        if (sharesPositive.isEmpty() && sharesNegative.isEmpty()) {
            if (transaction.size <= min) {
                minTransactions = transaction.map { it.copy() }
                min = transaction.size
            }
            return
        }

        if(transaction.size > min)
            return


        for (negIndex in sharesNegative.indices) {
            for (posIndex in sharesPositive.indices) {
                val sharePos = sharesPositive[posIndex]
                val shareNeg = sharesNegative[negIndex]

                if (sharePos.balanceAmount >= abs(shareNeg.balanceAmount)) {

                    val amountPayed = abs(shareNeg.balanceAmount)
                    sharePos.balanceAmount -= amountPayed
                    shareNeg.balanceAmount = 0F

                    if (amountPayed > 0F)
                        transaction.add(
                            BillShareBalance(
                                payedBy = shareNeg.person,
                                payedTo = sharePos.person,
                                amountPayed = amountPayed
                            )
                        )
                }

                else if (sharePos.balanceAmount < abs(shareNeg.balanceAmount)) {

                    val amountPayed = sharePos.balanceAmount
                    sharePos.balanceAmount = 0F
                    shareNeg.balanceAmount += amountPayed

                    if (amountPayed > 0F)
                        transaction.add(
                            BillShareBalance(
                                payedBy = shareNeg.person,
                                payedTo = sharePos.person,
                                amountPayed = amountPayed
                            )
                        )
                }
                val newSharePositive = arrayListOf<BillSpentAndShare>()
                val newSharesNegative = arrayListOf<BillSpentAndShare>()

                if (sharePos.balanceAmount == 0F) {
                    newSharePositive.addAll(sharesPositive.subList(0, posIndex))
                    newSharePositive.addAll(
                        sharesPositive.subList(
                            posIndex + 1,
                            sharesPositive.size
                        )
                    )
                } else {
                    newSharePositive.addAll(sharesPositive)
                }

                if (shareNeg.balanceAmount == 0F) {
                    newSharesNegative.addAll(sharesNegative.subList(0, negIndex))
                    newSharesNegative.addAll(
                        sharesNegative.subList(
                            negIndex + 1,
                            sharesNegative.size
                        )
                    )
                } else {
                    newSharesNegative.addAll(sharesNegative)
                }

                val posShareCopy = newSharePositive.map { it.copy() }
                val negShareCopy = newSharesNegative.map { it.copy() }

                splitBillAlgorithm(ArrayList(posShareCopy), ArrayList(negShareCopy), transaction)
            }
        }
    }

    fun splitBill() {
        var totalAmount = 0F

        bills.forEach { bill ->
            totalAmount += bill.total_amount ?: 0F
        }

        bills.forEach { bill ->
            bill.billShares?.forEach { billShare ->
                val shareAndSpent = BillSpentAndShare(billShare.user)
                spentAndShares.add(shareAndSpent)
                shareAndSpent.shareAmount += billShare.share ?: 0F
                shareAndSpent.spentAmount += billShare.spent ?: 0F
            }

        }

        spentAndShares.forEach { sAndS ->
            sAndS.balanceAmount = sAndS.spentAmount - sAndS.shareAmount
        }

        val posShares = arrayListOf<BillSpentAndShare>()
        val negShares = arrayListOf<BillSpentAndShare>()
        spentAndShares.forEach {
            if (it.balanceAmount > 0)
                posShares.add(it)
            else if (it.balanceAmount < 0)
                negShares.add(it)
        }

        val posShareCopy = posShares.map { it.copy() }
        val negShareCopy = negShares.map { it.copy() }

        splitBillAlgorithm(posShareCopy, negShareCopy, arrayListOf())

    }

    fun getBalances(): List<BillShareBalance>? = minTransactions

    fun getSharesAndBalance():List<BillSpentAndShare> = spentAndShares
}

data class BillShareBalance(
    val payedBy: User?,
    val payedTo: User?,
    val amountPayed: Float
)

data class BillSpentAndShare(
    val person: User?,
    var balanceAmount: Float = 0F,
    var shareAmount: Float = 0F,
    var spentAmount: Float = 0F
)