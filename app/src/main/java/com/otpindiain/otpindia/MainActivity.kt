package com.otpindiain.otpindia

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import com.otpindiain.otpindia.databinding.ActivityMainBinding
import com.otpindiain.otpindia.utils.Utils.showToast
import com.otpindiain.otpindia.webview.WebViewManager

class MainActivity : AppCompatActivity(), OnBackPressedDispatcherOwner {

    private var lastBackPressTime: Long = 0
    private lateinit var binding: ActivityMainBinding
    private val webViewManager: WebViewManager by lazy {
        WebViewManager(
            binding.webView,
            binding,
            DEFAULT_URL,
            NO_INTERNET
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        initializeUI()
    }

    private fun initializeUI() {
        setupWebView()
        setupOnBackPressedCallback()
    }

    private fun setupWebView() {
        webViewManager.showLogoAndProgressBar()
    }

    private fun setupOnBackPressedCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun handleBackPress() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < DOUBLE_CLICK_TIME_INTERVAL) {
            finish()
        } else {
            handleWebViewNavigation(currentTime)
        }
    }

    private fun handleWebViewNavigation(currentTime: Long) {
        if (webViewManager.webView.canGoBack()) {
            webViewManager.webView.goBack()
        } else {
            lastBackPressTime = currentTime
            showToast(getString(R.string.press_back_again_to_exit))
        }
    }

    companion object {
        private const val DOUBLE_CLICK_TIME_INTERVAL = 2000
        private const val DEFAULT_URL = "https://otpindia.in/"
        private const val NO_INTERNET = "No Internet Connectivity"
    }
}
