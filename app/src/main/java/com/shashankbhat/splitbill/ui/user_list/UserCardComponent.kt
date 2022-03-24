package com.shashankbhat.splitbill.ui.user_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.util.Status
import com.shashankbhat.splitbill.util.extension.badgeLayout
import com.shashankbhat.splitbill.util.extension.findActivity
import com.shashankbhat.splitbill.util.extension.getColor
import com.shashankbhat.splitbill.viewmodels.UserListViewModel

@Composable
fun UserCard(
    user: User? = null,
    viewModel: UserListViewModel? = null
) {

    val context = LocalContext.current

    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(6.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                val (bIcon, tvUserName, btnDeleteUser, btnLinkUser) = createRefs()


                Text(
                    text = if (user?.name?.length ?: 0 > 0) user?.name?.get(0).toString().uppercase() else "",
                    modifier = Modifier
                        .background((user?.name ?: "").getColor(), shape = CircleShape)
                        .badgeLayout()
                        .constrainAs(bIcon) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                    style = Typography.h5,
                    color = Color.White
                )

                Text(
                    text = user?.name ?: "",
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .fillMaxWidth()
                        .constrainAs(tvUserName) {
                            top.linkTo(parent.top)
                            start.linkTo(bIcon.end)
                            end.linkTo(btnDeleteUser.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        },
                    style = Typography.h6,
                )

                if (viewModel?.billList?.value?.data?.isEmpty() == true && viewModel.userListState.value.status != Status.Nothing)
                    IconButton(
                        onClick = {
                            viewModel.deleteUser(user)
                        },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .constrainAs(btnDeleteUser) {
                                top.linkTo(parent.top)
                                end.linkTo(btnLinkUser.start)
                            },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_delete),
                            contentDescription = "Delete member",
                            tint = Color.Red
                        )
                    }

                IconButton(
                    onClick = {
                        val dialog = LinkGroupMemberFragment(viewModel, user)
                        context.findActivity()?.supportFragmentManager?.let {
                            dialog.show(
                                it,
                                dialog.tag
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .constrainAs(btnLinkUser) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_link),
                        contentDescription = "Link member",
                        tint = Color(0xFF3EC590)
                    )
                }

            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplitBillTheme {
        UserCard(User("Shashank", 1))
    }
}