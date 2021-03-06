

package com.example.android.marsrealestate.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.bindRecyclerView
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding
import com.example.android.marsrealestate.network.MarsApiFilter


class OverviewFragment : Fragment() {


    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)
//        val binding = GridViewItemBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

//        viewModel.properties.observe(viewLifecycleOwner, Observer {
//            bindRecyclerView(PhotoGridAdapter, it)
//
//        })

        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        viewModel.apply {
            navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    findNavController().navigate(
                            OverviewFragmentDirections.actionShowDetail(it)
                    )

                }
            })
            displayPropertyDetailsComplete()
        }

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        viewModel.updateFilter(
        when (item?.itemId) {
            R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
            R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
            else -> MarsApiFilter.SHOW_ALL
        })

        return true
    }
}
