package com.yogeshlokhande.weathercheck.ui.main.view.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yogeshlokhande.weathercheck.R
import com.yogeshlokhande.weathercheck.data.model.LatLong
import com.yogeshlokhande.weathercheck.data.repository.CityRepository
import com.yogeshlokhande.weathercheck.databinding.FragmentBookmarkBinding
import com.yogeshlokhande.weathercheck.ui.base.BaseFragment
import com.yogeshlokhande.weathercheck.ui.main.adapter.BookmarkListAdapter
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.CityViewModel

class FragmentBookmark :
    BaseFragment<CityViewModel, FragmentBookmarkBinding, CityRepository>(),
    BookmarkListAdapter.DeleteCity, BookmarkListAdapter.ItemClickListener {

    private var cityList: List<LatLong> = mutableListOf()
    private lateinit var mAdapter: BookmarkListAdapter
    private val TAG = "FragmentBookmark"

    /**
     * Assigning viewmodel
     */
    override fun getViewModel() = CityViewModel::class.java

    /**
     * handles viewbinding
     */
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBookmarkBinding.inflate(inflater, container, false)

    /**
     * Passing repository
     */
    override fun getFragmentRepository() = CityRepository(requireActivity().application)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolBarBookmark.toolbar)
        binding.toolBarBookmark.toolbar.title = getString(R.string.title_bookmarkedcity)

        getAllCities()
    }

    /**
     * Get All bookmarked cities list and show it in recyclerview
     */
    private fun getAllCities() {
        viewModel.getAllCities()?.observe(requireActivity(), { it ->
            Log.d(TAG, "onViewCreated: " + it.size)
            cityList = it

            if (cityList.isEmpty()) {
                binding.llNoBookmark.visibility = View.VISIBLE
                binding.rvCitiesList.visibility = View.GONE
                binding.btnBookmark.setOnClickListener {
                    findNavController().navigate(R.id.action_fragmentBookmark_to_fragmentMaps)
                }
            } else {
                binding.llNoBookmark.visibility = View.GONE
                binding.rvCitiesList.visibility = View.VISIBLE
                setAdapter()
            }
        })
    }

    private fun setAdapter() {
        mAdapter = BookmarkListAdapter(
            this@FragmentBookmark,
            cityList,
            this@FragmentBookmark
        )
        val mLayoutManager = LinearLayoutManager(requireActivity())
        binding.rvCitiesList.layoutManager = mLayoutManager
        binding.rvCitiesList.layoutManager = mLayoutManager
        binding.rvCitiesList.itemAnimator = DefaultItemAnimator()
        binding.rvCitiesList.adapter = mAdapter
    }

    /**
     * Populates the app bar with the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bookmark, menu)
        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                myActionMenuItem.collapseActionView()
                if (cityList.isEmpty()) {
                    getAllCities()
                }
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                cityList = cityList.filter { it.address.toLowerCase().contains(s!!.toLowerCase()) }
                setAdapter()
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handles user clicks on menu items.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                findNavController().navigate(R.id.action_fragmentBookmark_to_fragmentMaps)
                true
            }
            R.id.action_help -> {
                findNavController().navigate(R.id.action_fragmentBookmark_to_fragmentWebView)
                true
            }
            R.id.action_reset -> {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Remove All")
                    .setMessage("Do you really want to remove all bookmarked cities?")
                    .setPositiveButton(getString(R.string.btn_remove)) { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                        viewModel.deleteAll()
                        mAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton(getString(R.string.btn_cancel)) { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }
                    .show()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    /**
     * Handles delete icon click to remove bookmarked city
     */
    override fun deleteOnClick(latLong: LatLong) {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Remove")
            .setMessage("Do you want remove this place from bookmark list?")
            .setPositiveButton(getString(R.string.btn_remove)) { dialogInterface, i ->
                dialogInterface.dismiss()
                viewModel.delete(latLong).observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        Toast.makeText(
                            requireContext(),
                            "Place removed from bookmark list.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                mAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(getString(R.string.btn_cancel)) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()

    }

    /**
     * Handles recyclerview item click
     */
    override fun onItemClicked(latLong: LatLong) {
        val bundle = bundleOf(getString(R.string.intent_lat_long) to latLong)
        findNavController().navigate(R.id.action_fragmentBookmark_to_fragmentViewDetails, bundle)
    }
}