package com.example.splitbill.ui.bill_shares

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.splitbill.model.BillShareModel
import com.example.splitbill.room_db.entity.User
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.ui.theme.Typography

@ExperimentalMaterialApi
@Composable
fun BillShareUser(
    user: User = User("Test", -1),
    billShareModel : BillShareModel = BillShareModel(0)
) {

    var spentAmount by remember {
        billShareModel.spent
    }

    var isEmptySpentAmount by remember {
        mutableStateOf(false)
    }

    var shareAmount by remember {
        billShareModel.share
    }

    var isEmptyShareAmount by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(vertical = 8.dp))
    ) {
        val (tvUserName, tfSpentAmount, tfShareAmount) = createRefs()

        Text(
            text = user.name,
            modifier = Modifier
                .constrainAs(tvUserName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(tfSpentAmount.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.wrapContent
                },
            style = Typography.h6
        )

        OutlinedTextField(
            value = spentAmount,
            onValueChange = {
                if(it.isNotEmpty())
                    isEmptySpentAmount = false
                spentAmount = it
            },
            modifier = Modifier
                .constrainAs(tfSpentAmount) {
                    top.linkTo(parent.top)
                    start.linkTo(tvUserName.end, margin = 8.dp)
                    end.linkTo(tfShareAmount.start)
                    width = Dimension.fillToConstraints
                },
            label = {
                Text(text = "Spent")
            },
            isError = isEmptySpentAmount
        )

        OutlinedTextField(
            value = shareAmount,
            onValueChange = {
                if(it.isNotEmpty())
                    isEmptyShareAmount = false
                shareAmount = it
            },
            modifier = Modifier
                .constrainAs(tfShareAmount) {
                    top.linkTo(tfSpentAmount.top)
                    start.linkTo(tfSpentAmount.end, margin = 8.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            label = {
                Text(text = "Share")
            },
            isError = isEmptyShareAmount,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun BillShareUserPreview() {
    SplitBillTheme {
        BillShareUser()
    }
}