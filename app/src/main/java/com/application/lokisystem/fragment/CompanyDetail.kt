package com.application.lokisystem.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import com.application.lokisystem.BuildConfig
import com.application.lokisystem.R
import com.application.lokisystem.databinding.FragmentCompanyDetailBinding


class CompanyDetail : Fragment() {
    lateinit var binding: FragmentCompanyDetailBinding

    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompanyDetailBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            activity?.finish()
        }
        val webView: WebView = binding.root.findViewById(R.id.webView)
        webView.loadUrl("${BuildConfig.URL_WEBVIEW_DETAIL}${arguments?.getString("code")}")
        webView.settings.apply {
            this.javaScriptEnabled = true
            this.useWideViewPort = true
            this.loadWithOverviewMode = true
            /**
             * This request has been blocked; the content must be served over HTTPS
             * https 에서 이미지가 표시 안되는 오류를 해결하기 위한 처리
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        val backTextView: TextView = binding.root.findViewById(R.id.back_btn)
        val navigationName: String? = arguments?.getString("navigationName")
        backTextView.text = navigationName

        return binding.root
    }
}