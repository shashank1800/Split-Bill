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
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.Groups
import com.shashankbhat.splitbill.databinding.AdapterGroupBinding
import com.shashankbhat.splitbill.databinding.FragmentGroupListBinding
import com.shashankbhat.splitbill.enums.SnackBarType
import com.shashankbhat.splitbill.util.extension.putToken
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupListFragment : Fragment() {
    private lateinit var binding: FragmentGroupListBinding
    private lateinit var navController: NavController
    private val viewModel: GroupListViewModel by activityViewModels()

    lateinit var adapter: RecyclerGenericAdapter<AdapterGroupBinding, GroupListDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllGroups()
    }

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
                    if((model.group.id ?: -1) > 0){
                        val bundle = Bundle()
                        bundle.putSerializable("model", model)
                        navController.navigate(R.id.nav_user_list, bundle)
                    }
                })
                add(CallBackModel(R.id.cv_root){ model, position, binding ->
                    if (model.userList.isEmpty()) {
                        binding.showSnackBar(
                                "Please add atleast one user to group",
                                "Okay",
                            snackBarType = SnackBarType.ERROR
                        )
                    } else {
                        val bundle = Bundle()
                        bundle.putSerializable("model", model)
                        navController.navigate(R.id.nav_bill_shares_view_pager, bundle)
                    }
                })
            })
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
        fun getInstance() = GroupListFragment()
    }
}