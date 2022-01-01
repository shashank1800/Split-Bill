package com.example.splitbill.util.extension

import com.example.splitbill.model.BillListDto
import kotlin.math.abs

class BillSplitAlgorithm(private val bills: List<List<BillListDto>?>) {

    private var min = Int.MAX_VALUE
    private var minTransactions: List<BS>? = null
    private var spentAndShares = arrayListOf<SS>()

    private fun splitBillAlgorithm(
        sharesPositive: List<SS>,
        sharesNegative: List<SS>,
        transaction: ArrayList<BS>
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
                            BS(
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
                            BS(
                                payedBy = shareNeg.person,
                                payedTo = sharePos.person,
                                amountPayed = amountPayed
                            )
                        )
                }
                val newSharePositive = arrayListOf<SS>()
                val newSharesNegative = arrayListOf<SS>()

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
            totalAmount += bill?.get(0)?.billDetails?.total_amount ?: 0F
        }

        bills[0]?.forEach {
            spentAndShares.add(SS(it))
        }

        bills.forEach { bill ->
            bill?.forEachIndexed { indexChild, billListDto ->
                spentAndShares[indexChild].shareAmount += billListDto.billDetails?.share ?: 0F
            }
        }

        bills.forEach { bill ->
            bill?.forEachIndexed { indexChild, billListDto ->
                spentAndShares[indexChild].spentAmount += billListDto.billDetails?.spent ?: 0F
            }
        }

        spentAndShares.forEach { ss ->
            ss.balanceAmount = ss.spentAmount - ss.shareAmount
        }

        val posShares = arrayListOf<SS>()
        val negShares = arrayListOf<SS>()
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

    fun getBalances(): List<BS>? = minTransactions

    fun getSharesAndBalance():List<SS> = spentAndShares
}

data class BS(
    val payedBy: BillListDto,
    val payedTo: BillListDto,
    val amountPayed: Float
)

data class SS(
    val person: BillListDto,
    var balanceAmount: Float = 0F,
    var shareAmount: Float = 0F,
    var spentAmount: Float = 0F
)