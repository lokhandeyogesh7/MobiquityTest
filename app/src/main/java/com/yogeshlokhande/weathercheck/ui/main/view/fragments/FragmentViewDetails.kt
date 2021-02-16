package com.yogeshlokhande.weathercheck.ui.main.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.yogeshlokhande.weathercheck.R
import com.yogeshlokhande.weathercheck.data.model.LatLong
import com.yogeshlokhande.weathercheck.data.repository.DetailsRepository
import com.yogeshlokhande.weathercheck.databinding.FragmentViewDetailsBinding
import com.yogeshlokhande.weathercheck.ui.base.BaseFragment
import com.yogeshlokhande.weathercheck.ui.main.adapter.ForecastAdapter
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.DetailsViewModel


class FragmentViewDetails :
    BaseFragment<DetailsViewModel, FragmentViewDetailsBinding, DetailsRepository>() {
    private val TAG = "FragmentViewDetails"
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    /**
     * Pass Viewmodel
     */
    override fun getViewModel() = DetailsViewModel::class.java

    /**
     * Assigning viewbinding
     */
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentViewDetailsBinding.inflate(inflater, container, false)

    /**
     * Pass repository
     */
    override fun getFragmentRepository() = DetailsRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUptoolbar()
        fetchTodaysForecast()
        fetchDataForFuture()
    }

    /**
     * Fetch data for future and populate recyclerview
     */
    private fun fetchDataForFuture() {
        viewModel.fetchForecast(latitude, longitude, getString(R.string.weatherappkey)).observe(
            viewLifecycleOwner,
            {
                binding.contentView.rvForecast.visibility = View.VISIBLE
                binding.contentView.progress.visibility = View.GONE
                if (it.list.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Sorry No data found.", Toast.LENGTH_SHORT).show()
                } else {
                    val mAdapter = ForecastAdapter(
                        it.list,
                    )
                    val mLayoutManager = LinearLayoutManager(requireActivity())
                    val dividerItemDecoration = DividerItemDecoration(
                        binding.contentView.rvForecast.context,
                        mLayoutManager.orientation
                    )
                    binding.contentView.rvForecast.layoutManager = mLayoutManager
                    binding.contentView.rvForecast.addItemDecoration(dividerItemDecoration)
                    binding.contentView.rvForecast.itemAnimator = DefaultItemAnimator()
                    binding.contentView.rvForecast.adapter = mAdapter
                }
            })

    }

    /**
     * Fetching data for today's forecast and setting it to the views
     */
    private fun fetchTodaysForecast() {
        viewModel.fetchTodaysForecast(latitude, longitude, getString(R.string.weatherappkey))
            .observe(viewLifecycleOwner,
                {
                    binding.rlTopView?.visibility = View.VISIBLE
                    binding.progressTop?.visibility = View.GONE
                    Log.d(TAG, "onViewCreated: " + it.name)
                    binding.tvEnvType.text = it.weather?.get(0)?.main
                    binding.tvPlaceName.text = it.weather?.get(0)?.description
                    binding.tvTemp.text = it.main?.temp.toString() + "\u2103"
                    binding.tvHumidity.text = "${it.main?.humidity} %"
                    binding.tvWind.text = "${it.wind?.speed} meter/sec"
                    Glide.with(this)
                        .load("http://openweathermap.org/img/w/${it.weather?.get(0)?.icon}.png")
                        .into(binding.imgBg)
                })
    }

    /**
     * Handles toolbar initialization,title settings and getting data from arguments
     */
    private fun setUptoolbar() {
        binding.contentView.rvForecast.visibility = View.GONE
        binding.contentView.progress.visibility = View.VISIBLE


        binding.rlTopView?.visibility = View.GONE
        binding.progressTop?.visibility = View.VISIBLE

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        var latLong: LatLong =
            arguments?.getParcelable<LatLong>(getString(R.string.intent_lat_long))!!
        latitude = latLong.latitude
        longitude = latLong.longitude
        binding.toolbarLayout.title = latLong.locality
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}