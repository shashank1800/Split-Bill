package com.shashankbhat.splitbill.ui.main_ui.nearby_people

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.shashankbhat.splitbill.ui.main_ui.group_list.AddGroupFragment
import com.shashankbhat.splitbill.util.LocationListener
import com.shashankbhat.splitbill.util.Response
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel

class NearbyPeopleFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentNearbyPeopleBinding
    private val myLocation = LocationListener()
    private val viewModel: GroupListViewModel by activityViewModels()

    lateinit var adapter: RecyclerGenericAdapter<AdapterNearbyUserBinding, NearUserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearbyPeopleBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isRefreshing = viewModel.isRefreshing
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
                    hideLoading()
                    binding.tvInstruction.text = ""
                }

                it.isLoading() -> {
                    showLoading()
                    binding.tvInstruction.text = ""
                }

                it.isError() -> {
                    hideLoading()
                    if (it.message != null)
                        binding.tvInstruction.text = it.message
                }

                else -> {
                    hideLoading()
                    binding.tvInstruction.text = ""
                }
            }
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

    @OptIn(ExperimentalComposeUiApi::class)
    private fun uiBtnCreateGroupClickListener() {
        binding.btnCreateGroup.setOnClickListener {
            val addGroupDialog = AddGroupFragment {
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
                binding.tvInstruction.text = "Permit app to use location to see nearby people"
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