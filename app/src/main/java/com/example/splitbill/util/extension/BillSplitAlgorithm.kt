package com.example.splitbill.util.extension

import com.example.splitbill.model.BillListDto
import kotlin.math.abs

class BillSplitAlgorithm(val bills: List<List<BillListDto>?>) {

    var min = Int.MAX_VALUE;
    var minTran: List<BS>? = null

    fun splitBillAlgo(
        sharesPositive: List<SS>,
        sharesNegative: List<SS>,
        transaction: ArrayList<BS>
    ) {
        if (sharesPositive.isEmpty() && sharesNegative.isEmpty()) {
            if (transaction.size <= min) {
                minTran = transaction.map { it.copy() }
                min = transaction.size
            }
            return
        }

        if(transaction.size > min)
            return


        for (negIndex in sharesNegative.indices) {
            for (posIndex in sharesPositive.indices) {
                val sharePos = sharesPositive.get(posIndex)
                val shareNeg = sharesNegative.get(negIndex)

                if (sharePos.shareAmount >= abs(shareNeg.shareAmount)) {

                    val amountPayed = abs(shareNeg.shareAmount)
                    sharePos.shareAmount -= amountPayed
                    shareNeg.shareAmount = 0F

                    if (amountPayed > 0F)
                        transaction.add(
                            BS(
                                payedBy = shareNeg.person,
                                payedTo = sharePos.person,
                                amountPayed = amountPayed
                            )
                        )
                }

                else if (sharePos.shareAmount < abs(shareNeg.shareAmount)) {

                    val amountPayed = sharePos.shareAmount
                    sharePos.shareAmount = 0F
                    shareNeg.shareAmount += amountPayed

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

                if (sharePos.shareAmount == 0F) {
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

                if (shareNeg.shareAmount == 0F) {
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

                splitBillAlgo(ArrayList(posShareCopy), ArrayList(negShareCopy), transaction)
            }
        }
    }

    fun splitBill() {
        var totalAmount = 0F

        bills.forEach { bill ->
            totalAmount += bill?.get(0)?.billDetails?.total_amount ?: 0F
        }

        val persons = bills[0]?.size ?: 0
        val totalAmountPerPerson = totalAmount / persons

        val sortedShares = arrayListOf<SS>()
        bills[0]?.forEach {
            sortedShares.add(SS(it))
        }

        bills.forEachIndexed { index, bill ->
            bill?.forEachIndexed { indexChild, billListDto ->
                sortedShares[indexChild].shareAmount += billListDto.billDetails?.spent ?: 0F
            }
        }

        sortedShares.forEachIndexed { index, ss ->
            ss.shareAmount -= totalAmountPerPerson
        }

        val posShares = arrayListOf<SS>()
        val negShares = arrayListOf<SS>()
        sortedShares.forEach {
            if (it.shareAmount > 0)
                posShares.add(it)
            else if (it.shareAmount < 0)
                negShares.add(it)
        }

        val posShareCopy = posShares.map { it.copy() }
        val negShareCopy = negShares.map { it.copy() }

        splitBillAlgo(posShareCopy, negShareCopy, arrayListOf())

        minTran
    }
}

data class BS(
    val payedBy: BillListDto,
    val payedTo: BillListDto,
    val amountPayed: Float
)

data class SS(
    val person: BillListDto,
    var shareAmount: Float = 0F
)

//fun List<List<BillListDto>>.get