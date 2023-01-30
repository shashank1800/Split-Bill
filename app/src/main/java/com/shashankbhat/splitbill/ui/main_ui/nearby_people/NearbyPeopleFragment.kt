package com.shashankbhat.splitbill.ui.main_ui.nearby_people

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.kotlinpermissions.KotlinPermissions
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.base.BaseFragment
import com.shashankbhat.splitbill.databinding.AdapterNearbyUserBinding
import com.shashankbhat.splitbill.databinding.FragmentNearbyPeopleBinding
import com.shashankbhat.splitbill.database.local.model.NearUserModel
import com.shashankbhat.splitbill.ui.main_ui.group_list.AddGroupDialogFragment
import com.shashankbhat.splitbill.util.LocationListener
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.MainScreenViewModel

class NearbyPeopleFragment : BaseFragment<FragmentNearbyPeopleBinding>(), SwipeRefreshLayout.OnRefreshListener {

    private val myLocation = LocationListener()
    private val viewModel: MainScreenViewModel by activityViewModels()

    lateinit var adapter: RecyclerGenericAdapter<AdapterNearbyUserBinding, NearUserModel>

    override fun getViewBinding() = FragmentNearbyPeopleBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isRefreshing = viewModel.isRefreshing
        binding.isNearbyPeopleEmpty = viewModel.isNearbyPeopleEmpty
        binding.srlGroupList.setOnRefreshListener(this)
        getNearUserList()

        uiRecyclerViewInit()
        uiBtnCreateGroupClickListener()
        networkNearUserResponse()
        networkAddGroupResponse()
    }

    private fun networkNearUserResponse() {
        viewModel.nearUserList.observe(viewLifecycleOwner) {
            when {
                it.isSuccess() -> {
                    adapter.replaceList(it.data ?: arrayListOf())
                }

                it.isError() -> {
                    if (it.message != null)
                        binding.tvInstr.text = it.message
                    adapter.replaceList(arrayListOf())
                }

                else -> {
                    binding.tvInstr.text = ""
                }
            }

            if((it.data?.size ?:0) == 0)
                viewModel.isNearbyPeopleEmpty.set(true)
            else
                viewModel.isNearbyPeopleEmpty.set(false)
        }
    }

    private fun networkAddGroupResponse() {
        viewModel.addGroupResponse.observe(viewLifecycleOwner) {
            if (it.isSuccess()) {
                viewModel.getAllGroups()
                binding.showSnackBar("Group created successfully")
                viewModel.vpBillShares?.setCurrentItem(0, true)

                for (nearUserModel in adapter.getItemList()) {
                    nearUserModel.isSelected.set(false)
                }
                createGroupButtonVisibility()
            }
        }

    }

    private fun uiRecyclerViewInit() {
        adapter = RecyclerGenericAdapter.Builder<AdapterNearbyUserBinding, NearUserModel>(
            R.layout.adapter_nearby_user,
            BR.model
        )
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterNearbyUserBinding, NearUserModel>>().apply {
                add(CallBackModel(R.id.main_card_view) { model, _, _ ->
                    if (model.isSelected.get()) deSelectUser(model)
                    else selectUser(model)
                })
            })
            .setMoreDataBinds(DataBinds().apply {
                add(MoreDataBindings(BR.sharedPref, viewModel.sharedPreferences))
            }).build()
        (binding.rvNearbyUsers.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter
    }

    private fun uiBtnCreateGroupClickListener() {
        binding.btnCreateGroup.setOnClickListener {
            val addGroupDialog = AddGroupDialogFragment.newInstance {
                viewModel.addGroupWithPeople(it)
            }
            addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)
        }
    }

    private fun getNearUserList() {
        KotlinPermissions.with(requireActivity())
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .onAccepted {
                viewModel.isRefreshing.set(true)
                findNearPeople()
            }.onDenied {
                requestUserToAllowPermission()
                viewModel.isRefreshing.set(false)
            }.onForeverDenied {
                binding.tvInstr.text = "Permit app to use location to see nearby people"
                viewModel.isRefreshing.set(false)
            }.ask()
    }

    private fun requestUserToAllowPermission() {
        binding.showSnackBar(
            "Permit app to use location to see nearby people",
            "Allow",
            actionListener = {
                getNearUserList()
            },
            duration = Snackbar.LENGTH_INDEFINITE
        )
    }

    private fun selectUser(model: NearUserModel) {
        model.isSelected.set(true)
        createGroupButtonVisibility()
    }

    private fun deSelectUser(model: NearUserModel) {
        model.isSelected.set(false)
        createGroupButtonVisibility()
    }

    private fun createGroupButtonVisibility() {
        var atLeastOneSelected = false
        for (model in adapter.getItemList()) {
            if (model.isSelected.get()) {
                atLeastOneSelected = true
                break
            }
        }

        if (atLeastOneSelected)
            binding.btnCreateGroup.visibility = View.VISIBLE
        else
            binding.btnCreateGroup.visibility = View.GONE
    }

    private fun findNearPeople() {
        viewModel.nearUserList.value = Response.loading()
        myLocation.getLocation(requireContext()) { location ->
            viewModel.getNearUser(location)
            myLocation.stopLocationUpdate()
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = NearbyPeopleFragment()
    }

    override fun onRefresh() {
        viewModel.isRefreshing.set(true)
        getNearUserList()
    }
}