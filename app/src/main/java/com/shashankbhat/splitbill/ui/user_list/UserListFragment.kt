package com.shashankbhat.splitbill.ui.user_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.database.local.dto.group_list.GroupListDto
import com.shashankbhat.splitbill.database.local.entity.User
import com.shashankbhat.splitbill.databinding.AdapterGroupUserBinding
import com.shashankbhat.splitbill.databinding.FragmentUserListBinding
import com.shashankbhat.splitbill.util.extension.findActivity
import com.shashankbhat.splitbill.viewmodels.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : BaseFragment<FragmentUserListBinding>() {

    private lateinit var navController: NavController
    private var groupListDto: GroupListDto? = null
    private val viewModel: UserListViewModel by viewModels()
    private val args: UserListFragmentArgs by navArgs()

    lateinit var adapter: RecyclerGenericAdapter<AdapterGroupUserBinding, User>

    override fun onStart() {
        super.onStart()
        setTitle("Group Members")
    }

    override fun getViewBinding() = FragmentUserListBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        groupListDto = args.model
        viewModel.getAllUsersByGroupId(groupListDto?.group?.id ?: -1)
        viewModel.getAllBill(groupListDto?.group?.id ?: -1)

        uiFabClickListener()
        uiRecyclerViewInit()

        networkUserListResponse()
    }

    private fun uiFabClickListener(){
        binding.fab.setOnClickListener {
            val addMember = AddGroupMembersFragment.newInstance {
                viewModel.addPeople(User(it, groupListDto?.group?.id ?: -1))
            }
            context?.findActivity()?.supportFragmentManager?.let {
                addMember.show(
                    it,
                    addMember.tag
                )
            }

        }
    }

    private fun uiRecyclerViewInit(){
        adapter = RecyclerGenericAdapter.Builder<AdapterGroupUserBinding, User>(R.layout.adapter_group_user, BR.model)
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterGroupUserBinding, User>>().apply {
                add(CallBackModel(R.id.iv_link){ model, _, _ ->
                    val dialog = LinkGroupMemberDialogFragment.newInstance {
                        viewModel.linkPeople(model, it)
                    }
                    context?.findActivity()?.supportFragmentManager?.let {
                        dialog.show(
                            it,
                            dialog.tag
                        )
                    }
                })
                add(CallBackModel(R.id.iv_delete){ model, _, _ ->
                    val deleteDialog = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete ${model.name}")
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteUser(model)
                        }.setNegativeButton("Cancel", null)
                    deleteDialog.show()
                })
            })
            .setMoreDataBinds(DataBinds().apply {
                add(MoreDataBindings(BR.isBillListEmpty, viewModel.isBillListEmpty))
                add(MoreDataBindings(BR.sharedPref, viewModel.sharedPreferences))
            })
            .build()

        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter
    }

    private fun networkUserListResponse(){
        viewModel.userListState.observe(viewLifecycleOwner) {
            if(it.isSuccess()){
                if(adapter.getItemList().size == it.data?.size){
                    val oldList = adapter.getItemList()
                    val newList = it.data
                    val listSize = it.data.size
                    for(index in 0 until listSize){
                        if(newList[index] != oldList[index]){
                            // Replace complete group
                            adapter.replaceItemAt(index, newList[index])
                        }
                    }
                }else
                    adapter.replaceList(ArrayList(it.data ?: emptyList()))
            }
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = UserListFragment()
    }
}