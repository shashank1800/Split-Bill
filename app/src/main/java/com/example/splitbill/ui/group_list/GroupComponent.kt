package com.example.splitbill.ui.group_list

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.splitbill.R
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.ui.theme.Typography
import com.example.splitbill.viewmodels.GroupListViewModel

@ExperimentalMaterialApi
@Composable
fun GroupCard(
    group: GroupListModel,
    viewModel: GroupListViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                val bundle = Bundle()
                bundle.putSerializable("model", group)
                navController.navigate(R.id.nav_bill_shares, bundle)
            }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val (tvGroupName, btnAddMember, tvCount) = createRefs()

                Text(
                    text = group.group.name,
                    modifier = Modifier
                        .constrainAs(tvGroupName) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(btnAddMember.start)
                            width = Dimension.fillToConstraints
                        },
                    style = Typography.h6
                )

                Text(
                    text = group.userList.size.toString(),
                    modifier = Modifier
                        .constrainAs(tvCount) {
                            top.linkTo(tvGroupName.bottom)
                            start.linkTo(parent.start)
                        },
                    style = Typography.caption
                )

                IconButton(
                    onClick = {
                        val bundle = Bundle()
                        bundle.putSerializable("model", group)
                        navController.navigate(R.id.nav_user_list, bundle)

                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .constrainAs(btnAddMember) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_user_view),
                        contentDescription = "add member",
                    )
                }

            }
        }
    }
}