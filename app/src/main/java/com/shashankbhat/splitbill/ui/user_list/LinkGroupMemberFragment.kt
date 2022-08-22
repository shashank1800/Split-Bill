package com.shashankbhat.splitbill.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.viewmodels.UserListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashankbhat.splitbill.util.SplitBillTheme

class LinkGroupMemberFragment(
    private val viewModel: UserListViewModel? = null,
    private val user: User? = null
) :
    BottomSheetDialogFragment() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    LinkUserWithUniqueId()
                }
            }
        }
    }


    @ExperimentalComposeUiApi
    @Composable
    fun LinkUserWithUniqueId() {
        val keyboardController = LocalSoftwareKeyboardController.current

        var uniqueId by remember {
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
                text = "Link Member",
                modifier = Modifier.constrainAs(tvCreateGroup) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            OutlinedTextField(
                value = uniqueId,
                onValueChange = {
                    if (it.isNotEmpty())
                        isEmpty = false
                    uniqueId = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(tfGroupName) {
                        top.linkTo(tvCreateGroup.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                label = {
                    Text(text = "Enter #ID")
                },
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                isError = isEmpty,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    if (uniqueId.isNullOrEmpty()) {
                        isEmpty = true
                    } else {
                        viewModel?.linkPeople(user, uniqueId)
                        dialog?.cancel()
                    }
                },
                modifier = Modifier.constrainAs(btnCreate) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            ) {
                Text("Link")
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