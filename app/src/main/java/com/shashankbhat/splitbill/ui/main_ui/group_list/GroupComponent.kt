package com.shashankbhat.splitbill.ui.main_ui.group_list

import android.os.Bundle
import android.text.format.DateUtils.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.ui.theme.Typography
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun GroupCard(
    group: GroupListDto,
    scaffoldState: ScaffoldState,
    navController: NavController
) {
    val context = LocalContext.current
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
                if (group.userList.isEmpty()) {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Please add atleast one user to group",
                            "Okay"
                        )
                    }
                } else {
                    val bundle = Bundle()
                    bundle.putSerializable("model", group)
                    navController.navigate(R.id.nav_bill_shares_view_pager, bundle)
                }
            },
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val (tvGroupName, btnAddMember, tvCount, tvTime) = createRefs()

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
                    text = group.group.usersCount.toString(),
                    modifier = Modifier
                        .constrainAs(tvCount) {
                            top.linkTo(tvGroupName.bottom)
                            start.linkTo(parent.start)
                        },
                    style = Typography.body1
                )

                Text(
                    text = getRelativeDateTimeString(
                        context, group.group.dateCreated,
                        MINUTE_IN_MILLIS,
                        WEEK_IN_MILLIS,
                        FORMAT_SHOW_TIME
                    ).toString(),
                    modifier = Modifier
                        .constrainAs(tvTime) {
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        },
                    style = Typography.caption
                )

                IconButton(
                    onClick = {
                        if(group.group.id ?: -1 > 0){
                            val bundle = Bundle()
                            bundle.putSerializable("model", group)
                            navController.navigate(R.id.nav_user_list, bundle)
                        }
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
                        painter = painterResource(R.drawable.ic_outline_people),
                        tint = Color(0xFF3EC590),
                        contentDescription = "add member",
                    )
                }

            }
        }
    }
}