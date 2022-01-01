package com.example.splitbill.ui.group_list

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.splitbill.R
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.ui.theme.Typography
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun GroupCard(
    group: GroupListModel,
    scaffoldState: ScaffoldState,
    navController: NavController
) {

    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                if(group.userList.isEmpty()){
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Please add atleast one user to group" ,"Okay")
                    }
                }else{
                    val bundle = Bundle()
                    bundle.putSerializable("model", group)
                    navController.navigate(R.id.nav_bill_shares, bundle)
                }
            },
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
                        painter = painterResource(R.drawable.ic_baseline_people),
                        tint = Color(0xFF3EC590),
                        contentDescription = "add member",
                    )
                }

            }
        }
    }
}