package com.example.splitbill.ui.bill_shares

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.splitbill.model.BillListModel
import com.example.splitbill.ui.theme.Typography

@ExperimentalMaterialApi
@Composable
fun BillCard(
    billListModel: BillListModel
) {

    Box(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),

        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val (tvBillName, lcShare) = createRefs()

                Text(
                    text = billListModel.bill?.name.toString(),
                    modifier = Modifier
                        .constrainAs(tvBillName) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    style = Typography.h6
                )

                LazyColumn(modifier = Modifier
                    .height(50.dp)
                    .constrainAs(lcShare) {
                        top.linkTo(tvBillName.bottom)
                        start.linkTo(parent.start)
                        height = Dimension.wrapContent
                    },
                ){
                    items(billListModel.userList?: emptyList()){ bill ->
                        Text(
                            text = bill.user_id.toString() + " " + bill.share.toString() + " " + bill.spent.toString(),
                            style = Typography.caption
                        )
                    }
                }

            }
        }
    }
}