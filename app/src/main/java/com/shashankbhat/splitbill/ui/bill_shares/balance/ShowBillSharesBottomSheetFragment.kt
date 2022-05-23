package com.shashankbhat.splitbill.ui.bill_shares.balance

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
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.alogrithm.BillSplitAlgorithm
import com.shashankbhat.splitbill.base.TitleFragment
import com.shashankbhat.splitbill.database.local.dto.bill_shares.BillModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowBillSharesBottomSheetFragment : TitleFragment() {

    private val billSplitAlgorithm: MutableState<BillSplitAlgorithm> = mutableStateOf(BillSplitAlgorithm(emptyList()))

    fun setBill(bills: List<BillModel>){
        billSplitAlgorithm.value = BillSplitAlgorithm(bills)
    }

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

            if(billSplitAlgorithm.value.getBalances()?.size != 0){
                Text(
                    text = "Balance",
                    style = Typography.h6
                )

                LazyColumn {
                    items(billSplitAlgorithm.value.getBalances() ?: emptyList()) { billShare ->
                        BalanceCard(billShare)
                    }
                }

            }

            Text(
                text = "Total",
                modifier = Modifier.padding(top = 8.dp),
                style = Typography.h6
            )

            SpentAndShareHead()

            LazyColumn {
                items(billSplitAlgorithm.value.getSharesAndBalance()) { shareAndBalance ->
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

    companion object {
        fun getInstance(): ShowBillSharesBottomSheetFragment {
            return ShowBillSharesBottomSheetFragment()
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
