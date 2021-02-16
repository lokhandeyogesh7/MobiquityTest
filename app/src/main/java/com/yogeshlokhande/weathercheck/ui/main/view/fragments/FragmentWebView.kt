package com.yogeshlokhande.weathercheck.ui.main.view.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.yogeshlokhande.weathercheck.data.repository.WebViewRepository
import com.yogeshlokhande.weathercheck.databinding.FragmentWebviewBindingBinding
import com.yogeshlokhande.weathercheck.ui.base.BaseFragment
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.WebViewModel

class FragmentWebView :
    BaseFragment<WebViewModel, FragmentWebviewBindingBinding, WebViewRepository>() {

    override fun getViewModel() = WebViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWebviewBindingBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = WebViewRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUptoolbar()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.webView.visibility = View.GONE
                binding.progressWeb.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webView.visibility = View.VISIBLE
                binding.progressWeb.visibility = View.GONE
            }
        }
        binding.webView.loadUrl("https://openweathermap.org/")
    }

    /**
     * Handles toolbar initialization,title settings
     */
    private fun setUptoolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.webToolbar.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.webToolbar.toolbar.title = "Help"
        binding.webToolbar.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}