package com.shashankbhat.splitbill.ui.main_ui.group_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.databinding.AdapterGroupBinding
import com.shashankbhat.splitbill.databinding.AdapterGroupUserBinding
import com.shashankbhat.splitbill.databinding.FragmentGroupListBinding
import com.shashankbhat.splitbill.databinding.FragmentUserListBinding
import com.shashankbhat.splitbill.ui.user_list.AddGroupMemberFragment
import com.shashankbhat.splitbill.ui.user_list.LinkGroupMemberFragment
import com.shashankbhat.splitbill.ui.user_list.UserList
import com.shashankbhat.splitbill.util.extension.findActivity
import com.shashankbhat.splitbill.util.extension.putToken
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import com.shashankbhat.splitbill.viewmodels.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupList : Fragment() {
    private lateinit var binding: FragmentGroupListBinding
    private lateinit var navController: NavController
    private val viewModel: GroupListViewModel by activityViewModels()

    lateinit var adapter: RecyclerGenericAdapter<AdapterGroupBinding, GroupListDto>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupListBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isGroupListEmpty = viewModel.isGroupListEmpty

        navController = findNavController()
        viewModel.getAllGroups()

        viewModel.unauthorized.observe(viewLifecycleOwner) {
            if(it == true){
                viewModel.sharedPreferences.putToken("")
                navController.navigate(R.id.nav_splash_screen)
            }

        }

        binding.fab.setOnClickListener {

            val addGroupDialog = AddGroupFragment{
                viewModel.addGroup(Groups(it, usersCount = 0))
            }
            addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)

        }

        adapter = RecyclerGenericAdapter.Builder<AdapterGroupBinding, GroupListDto>(R.layout.adapter_group, BR.model)
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterGroupBinding, GroupListDto>>().apply {
                add(CallBackModel(R.id.iv_user_icon){ model, position, binding ->

                })
                add(CallBackModel(R.id.cv_root){ model, position, binding ->
                    if((model.group.id ?: -1) > 0){
                        val bundle = Bundle()
                        bundle.putSerializable("model", model)
                        navController.navigate(R.id.nav_user_list, bundle)
                    }
                })
            })
//            .setMoreDataBinds(DataBinds().apply {
//                add(MoreDataBindings(BR.isBillListEmpty, viewModel.isBillListEmpty))
//            })
            .build()

        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        viewModel.groupsListState.observe(viewLifecycleOwner) {
            if(it.isSuccess()) {
                adapter.replaceList(ArrayList(it.data ?: emptyList()))
                viewModel.isGroupListEmpty.set(it.data?.size == 0)
            }
        }


    }

    companion object {
        @JvmStatic
        fun getInstance() = GroupList()
    }
}