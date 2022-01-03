package com.shashankbhat.splitbill.ui.bill_shares

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.extension.SS

@Composable
fun SpentAndShareCard(shareAndBalance: SS) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (tvUserName, tvSpentAmount, tvShareAmount, tvBalance) = createRefs()

        Text(
            text = shareAndBalance.person.billDetails?.user_name ?: "",
            modifier = Modifier.constrainAs(tvUserName) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(tvSpentAmount.start, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )

        Text(
            text = shareAndBalance.spentAmount.toString(),
            modifier = Modifier.constrainAs(tvSpentAmount) {
                top.linkTo(parent.top)
                start.linkTo(tvUserName.end)
                end.linkTo(tvShareAmount.start, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )

        Text(
            text = shareAndBalance.shareAmount.toString(),
            modifier = Modifier.constrainAs(tvShareAmount) {
                top.linkTo(parent.top)
                start.linkTo(tvSpentAmount.end)
                end.linkTo(tvBalance.end)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )

        Text(
            text = shareAndBalance.balanceAmount.toString(),
            modifier = Modifier.constrainAs(tvBalance) {
                top.linkTo(parent.top)
                start.linkTo(tvShareAmount.end)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            style = Typography.body1
        )
    }
}