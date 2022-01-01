package com.example.splitbill.ui.bill_shares

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.ui.theme.Typography
import com.example.splitbill.util.extension.BillSplitAlgorithm
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShowBillSharesBottomSheetFragment(private val billSplitAlgorithm: BillSplitAlgorithm) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    ShowBillShares()
                }
            }
        }
    }


    @Composable
    fun ShowBillShares() {

        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

            Text(
                text = "Balance",
                style = Typography.h6
            )

            LazyColumn {
                items(billSplitAlgorithm.getBalances() ?: emptyList()) { billShare ->
                    BalanceCard(billShare)
                }
            }

            Text(
                text = "Total",
                modifier = Modifier.padding(top = 8.dp),
                style = Typography.h6
            )

            SpentAndShareHead()

            LazyColumn {
                items(billSplitAlgorithm.getSharesAndBalance()) { shareAndBalance ->
                    SpentAndShareCard(shareAndBalance)
                }
            }


        }

    }

    @Composable
    fun SpentAndShareHead() {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (tvUserName, tvSpentAmount, tvShareAmount, tvBalance) = createRefs()

            Text(
                text = "Name",
                modifier = Modifier.constrainAs(tvUserName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(tvSpentAmount.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
                style = Typography.h1
            )

            Text(
                text = "Spent",
                modifier = Modifier.constrainAs(tvSpentAmount) {
                    top.linkTo(parent.top)
                    start.linkTo(tvUserName.end)
                    end.linkTo(tvShareAmount.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
                style = Typography.h1
            )

            Text(
                text = "Share",
                modifier = Modifier.constrainAs(tvShareAmount) {
                    top.linkTo(parent.top)
                    start.linkTo(tvSpentAmount.end)
                    end.linkTo(tvBalance.end)
                    width = Dimension.fillToConstraints
                },
                style = Typography.h1
            )

            Text(
                text = "Balance",
                modifier = Modifier.constrainAs(tvBalance) {
                    top.linkTo(parent.top)
                    start.linkTo(tvShareAmount.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                style = Typography.h1
            )
        }
    }

//    @Preview(showBackground = true)
//    @Composable
//    fun ShowBillSharesPreview() {
//        SplitBillTheme {
//            ShowBillShares()
//        }
//    }


}
