package com.shashankbhat.splitbill.ui.main_ui.group_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupRecyclerListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.databinding.AdapterGroupBinding
import com.shashankbhat.splitbill.databinding.FragmentGroupListBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel

class GroupListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentGroupListBinding
    private lateinit var navController: NavController
    private val viewModel: GroupListViewModel by activityViewModels()

    lateinit var adapter: RecyclerGenericAdapter<AdapterGroupBinding, GroupRecyclerListDto>

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
        binding.isRefreshing = viewModel.isRefreshing
        navController = findNavController()

        binding.srlGroupList.setOnRefreshListener(this)

        viewModel.unauthorized.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.unauthorized.value = false
                navController.navigate(R.id.nav_splash_screen)
            }

        }

        binding.fab.setOnClickListener {

            val addGroupDialog = AddGroupFragment {
                viewModel.addGroup(Groups(it))
            }
            addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)

        }

        adapter = RecyclerGenericAdapter.Builder<AdapterGroupBinding, GroupRecyclerListDto>(
            R.layout.adapter_group,
            BR.model
        )
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterGroupBinding, GroupRecyclerListDto>>().apply {
                add(CallBackModel(R.id.iv_user_icon) { model, position, binding ->
                    if ((model.group?.id ?: -1) > 0) {
                        val bundle = Bundle()
                        bundle.putSerializable(
                            "model",
                            GroupListDto(model.group!!, model.userList ?: emptyList())
                        )
                        navController.navigate(R.id.nav_user_list, bundle)
                    }
                })
                add(CallBackModel(R.id.cv_root) { model, position, binding ->
                    navigateToBillSharesScreen(model)
                })
            })
            .build()

        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        viewModel.groupsListState.observe(viewLifecycleOwner) {
            if (it.isSuccess()) {
                viewModel.isGroupListEmpty.set(it.data?.size == 0)
                if (adapter.getItemList().size == it.data?.size) {
                    val oldList = adapter.getItemList()
                    val newList = it.data
                    val listSize = it.data.size
                    for (index in 0 until listSize) {
                        if (newList[index].group == oldList[index].group
                            && newList[index].userList != oldList[index].userList
                        ) {
                            // Replace only user list
                            newList[index].userList?.let { uList ->
                                oldList[index].adapter?.replaceList(ArrayList(uList.take(3)))
                            }
                        } else if (newList[index].group != oldList[index].group) {

                            // Replace complete group
                            adapter.replaceItemAt(index,
                                GroupRecyclerListDto(
                                    newList[index].group,
                                    newList[index].userList,
                                    null
                                )
                            )
                        }
                    }
                } else
                    adapter.replaceList(ArrayList(it.data ?: emptyList()))

            }
        }


    }

    private fun navigateToBillSharesScreen(model: GroupRecyclerListDto) {
        if (model.userList != null && model.userList.isEmpty()) {
            binding.showSnackBar(
                "Please add atleast one user to group",
                "Okay",
                snackBarType = SnackBarType.ERROR
            )
        } else {
            val bundle = Bundle()
            bundle.putSerializable(
                "model", GroupListDto(
                    model.group!!,
                    model.userList ?: emptyList()
                )
            )
            navController.navigate(R.id.nav_bill_shares_view_pager, bundle)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = GroupListFragment()
    }

    override fun onRefresh() {
        viewModel.isRefreshing.set(true)
        viewModel.getAllGroups()
    }
}