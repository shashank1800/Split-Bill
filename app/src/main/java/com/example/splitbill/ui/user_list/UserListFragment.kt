package com.example.splitbill.ui.user_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.splitbill.ui.theme.SplitBillTheme
import com.example.splitbill.viewmodels.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.splitbill.R
import com.example.splitbill.model.GroupListModel
import com.example.splitbill.util.extension.findActivity

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var model: GroupListModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()

        model = requireArguments().getSerializable("model") as GroupListModel
        viewModel.getAllUsersByGroupId(model.group.id)

        return ComposeView(requireContext()).apply {
            setContent {
                SplitBillTheme {
                    UserList()
                }
            }
        }
    }


    @Composable
    fun UserList() {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                        val addMember = AddGroupMemberFragment(viewModel, model)
                        context?.findActivity()?.supportFragmentManager?.let { addMember.show(it, addMember.tag) }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_person_add),
                        contentDescription = "Add Member"
                    )
                }
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.userListState.value) { user ->
                    UserCard(user = user, viewModel)
                }
            }

        }
    }

}