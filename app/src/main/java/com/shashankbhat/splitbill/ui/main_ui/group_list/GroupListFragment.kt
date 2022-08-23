package com.shashankbhat.splitbill.ui.main_ui.group_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
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
import com.shashankbhat.splitbill.ui.main_ui.HomeScreenViewPagerDirections
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.isGroupListEmpty = viewModel.isGroupListEmpty
        binding.isRefreshing = viewModel.isRefreshing
        navController = findNavController()
        binding.srlGroupList.setOnRefreshListener(this)

        uiFabClickListener()
        uiRecyclerViewInit()

        networkUnauthorizedResponse()
        networkGroupListResponse()
    }

    private fun networkUnauthorizedResponse() {
        viewModel.unauthorized.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.unauthorized.value = false
                navController.navigate(HomeScreenViewPagerDirections.actionNavHomeScreenToNavSplashScreen())
            }

        }
    }

    private fun uiFabClickListener() {
        binding.fab.setOnClickListener {
            // Error handle
            val addGroupDialog = AddGroupDialogFragment.newInstance {
                viewModel.addGroup(Groups(it))
            }
            addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)
        }
    }

    private fun uiRecyclerViewInit() {
        adapter = RecyclerGenericAdapter.Builder<AdapterGroupBinding, GroupRecyclerListDto>(
            R.layout.adapter_group,
            BR.model
        )
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterGroupBinding, GroupRecyclerListDto>>().apply {
                add(CallBackModel(R.id.iv_user_icon) { model, position, binding ->
                    if ((model.group?.id ?: -1) > 0) {
                        navController.navigate(
                            HomeScreenViewPagerDirections.actionNavGroupListToNavUserList(
                                model.group?.let {
                                    GroupListDto(
                                        it, model.userList ?: emptyList()
                                    )
                                })
                        )
                    }
                })
                add(CallBackModel(R.id.cv_root) { model, _, _ ->
                    navigateToBillSharesScreen(model)
                })
            })
            .build()
        (binding.rvNearbyUsers.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter
    }

    private fun networkGroupListResponse() {
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
                            adapter.replaceItemAt(
                                index,
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
            val groupListDto = GroupListDto(
                model.group!!,
                model.userList ?: emptyList()
            )
            navController.navigate(
                HomeScreenViewPagerDirections.actionNavGroupListToNavBillSharesViewPager(
                    groupListDto
                )
            )
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