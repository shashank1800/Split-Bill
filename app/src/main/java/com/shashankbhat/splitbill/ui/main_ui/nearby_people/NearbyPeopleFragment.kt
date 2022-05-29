package com.shashankbhat.splitbill.ui.main_ui.nearby_people

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinpermissions.KotlinPermissions
import com.shashankbhat.splitbill.BR
import com.shashankbhat.splitbill.R
import com.shashankbhat.splitbill.database.remote.entity.NearUserDto
import com.shashankbhat.splitbill.databinding.AdapterNearbyUserBinding
import com.shashankbhat.splitbill.databinding.FragmentNearbyPeopleBinding
import com.shashankbhat.splitbill.util.LocationListener
import com.shashankbhat.splitbill.util.recyclergenericadapter.MoreDataBindings
import com.shashankbhat.splitbill.util.recyclergenericadapter.RecyclerGenericAdapter
import com.shashankbhat.splitbill.viewmodels.GroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearbyPeopleFragment : Fragment() {
    private lateinit var binding: FragmentNearbyPeopleBinding
    private val myLocation = LocationListener()
    private val viewModel: GroupListViewModel by activityViewModels()

    lateinit var adapter: RecyclerGenericAdapter<AdapterNearbyUserBinding, NearUserDto>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNearbyPeopleBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.key.setOnClickListener {

            KotlinPermissions.with(requireActivity())
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .onAccepted { permissions -> findNearPeople() }.ask()

        }

        adapter = RecyclerGenericAdapter(R.layout.adapter_nearby_user, BR.model,
            moreDataBinds = arrayListOf<MoreDataBindings<Any, Int>>().apply {
                add(MoreDataBindings(viewModel.sharedPreferences, BR.sharedPref))
            }
        )
        binding.rvNearbyUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearbyUsers.adapter = adapter

        lifecycleScope.launch{
            viewModel.nearUserList.collect {
                adapter.submitList(it.users)
            }
        }

    }

    private fun findNearPeople(){
        myLocation.getLocation(requireContext()){ location ->
            /*
            * 0.4 0.4 - 48 km
            * 0 0.1 - 4.87
            * 0.1 0.1 - 12.1
            * */
//            val locationFrom = LatLong(64.0, 175.0)
//            val locationTo = LatLong(64.0, 177.5)
//            Log.i("NearbyPeopleFragment", " ${locationFrom.findDistance(locationTo)}")

            viewModel.getNearUser(location)
            myLocation.stopLocationUpdate()
        }
    }

    companion object {
        @JvmStatic
        fun getInstance() = NearbyPeopleFragment()
    }
}