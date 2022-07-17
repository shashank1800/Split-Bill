package com.shashankbhat.splitbill.ui.main_ui.nearby_people

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinpermissions.KotlinPermissions
import com.shahankbhat.recyclergenericadapter.RecyclerGenericAdapter
import com.shahankbhat.recyclergenericadapter.util.CallBackModel
import com.shahankbhat.recyclergenericadapter.util.DataBinds
import com.shahankbhat.recyclergenericadapter.util.MoreDataBindings
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.databinding.AdapterNearbyUserBinding
import com.shashankbhat.splitbill.databinding.FragmentNearbyPeopleBinding
import com.shashankbhat.splitbill.model.NearUserModel
import com.shashankbhat.splitbill.ui.main_ui.group_list.AddGroupFragment
import com.shashankbhat.splitbill.util.LocationListener
import com.shashankbhat.splitbill.util.extension.showSnackBar
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearbyPeopleFragment : Fragment() {
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

        binding.key.setOnClickListener {

            KotlinPermissions.with(requireActivity())
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .onAccepted { permissions -> findNearPeople() }.ask()

        }

        adapter = RecyclerGenericAdapter.Builder<AdapterNearbyUserBinding, NearUserModel>(R.layout.adapter_nearby_user, BR.model)
            .setClickCallbacks(arrayListOf<CallBackModel<AdapterNearbyUserBinding, NearUserModel>>().apply {
                add(CallBackModel(R.id.main_card_view){ model, position, binding ->
                    if(model.isSelected.get()) deSelectUser(model)
                    else selectUser(model)
                })
            })
            .setMoreDataBinds(DataBinds().apply {
                add(MoreDataBindings(BR.sharedPref, viewModel.sharedPreferences))
            }).build()

        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        lifecycleScope.launch{
            viewModel.nearUserList.collect {
                adapter.replaceList(it)
            }
        }

        binding.btnCreateGroup.setOnClickListener {
            val addGroupDialog = AddGroupFragment {
                viewModel.addGroupWithPeople(it)
            }
            addGroupDialog.show(parentFragmentManager, addGroupDialog.tag)
        }

        viewModel.addGroupResponse.observe(viewLifecycleOwner) {

            if(it.isSuccess()){
                binding.showSnackBar("Group created successfully")
            }
        }

    }

    private fun selectUser(model : NearUserModel){
        model.isSelected.set(true)
        createGroupButtonVisibility()
    }
    private fun deSelectUser(model : NearUserModel){
        model.isSelected.set(false)
        createGroupButtonVisibility()
    }

    private fun createGroupButtonVisibility(){
        var atLeastOneSelected = false
        for(model in adapter.getItemList()) {
            if(model.isSelected.get()) {
                atLeastOneSelected = true
                break
            }
        }

        if(atLeastOneSelected)
            binding.btnCreateGroup.visibility = View.VISIBLE
        else
            binding.btnCreateGroup.visibility = View.GONE
    }

    private fun findNearPeople(){
        myLocation.getLocation(requireContext()){ location ->
            viewModel.getNearUser(location)
            myLocation.stopLocationUpdate()
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = NearbyPeopleFragment()
    }
}