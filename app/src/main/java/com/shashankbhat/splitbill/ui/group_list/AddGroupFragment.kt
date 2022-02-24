package com.shashankbhat.splitbill.ui.group_list

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.System.getString
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shashankbhat.splitbill.room_db.entity.Groups
import com.shashankbhat.splitbill.ui.theme.SplitBillTheme
import com.shashankbhat.splitbill.ui.theme.Typography
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddGroupFragment(private val viewModel: GroupListViewModel) : BottomSheetDialogFragment() {

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    CreateGroupCard()
                }
            }
        }
    }


    @ExperimentalComposeUiApi
    @Composable
    fun CreateGroupCard() {
        val keyboardController = LocalSoftwareKeyboardController.current

        var groupName by remember {
            mutableStateOf("")
        }
        var isEmpty by remember {
            mutableStateOf(false)
        }


        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
        ) {
            val (tvCreateGroup, tfGroupName, btnCreate, btnCancel) = createRefs()

            Text(
                text = "Create Group",
                modifier = Modifier.constrainAs(tvCreateGroup) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = Typography.h6
            )

            OutlinedTextField(
                value = groupName,
                onValueChange = {
                    if(it.isNotEmpty())
                        isEmpty = false
                    groupName = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(tfGroupName) {
                        top.linkTo(tvCreateGroup.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                label = {
                    Text(text = "Enter Name")
                },
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}
                ),
                isError = isEmpty
            )

            Button(
                onClick = {
                    if(groupName.isNullOrEmpty()){
                        isEmpty = true
                    }else{
                        viewModel.addGroup(Groups(groupName))
                        dialog?.cancel()
                    }
                },
                modifier = Modifier.constrainAs(btnCreate) {
                    top.linkTo(tfGroupName.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
                },
            ) {
                Text("Create")
            }

            OutlinedButton(
                onClick = { dialog?.cancel() },
                modifier = Modifier.constrainAs(btnCancel) {
                    top.linkTo(btnCreate.top)
                    bottom.linkTo(btnCreate.bottom)
                    end.linkTo(btnCreate.start, margin = 16.dp)
                }
            ) {
                Text("Cancel")
            }

        }

    }

}