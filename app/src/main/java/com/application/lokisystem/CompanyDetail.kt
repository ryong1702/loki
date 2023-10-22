package com.application.lokisystem

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.application.lokisystem.databinding.ActivityCompanyDetailBinding

class CompanyDetail : AppCompatActivity() {
    private lateinit var binding : ActivityCompanyDetailBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = intent.getStringExtra("navigationName")
            customView = actionBarCustomView()
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF000000")))
        }
        val intent = intent
        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl("${BuildConfig.URL_WEBVIEW_DETAIL}${intent.getStringExtra("code")}")
        webView.settings.apply {
            this.javaScriptEnabled = true
            this.useWideViewPort = true
            this.loadWithOverviewMode = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }
    @SuppressLint("RtlHardcoded")
    private fun actionBarCustomView(): TextView {
        return TextView(binding.root.context).apply {

            // Initialize layout parameters for text view
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )

            // Apply layout params for text view
            layoutParams = params

            text = intent.getStringExtra("navigationName")
            // Center align the text
            gravity = Gravity.LEFT

            // Title text appearance
            setTextAppearance(
                android.R.style
                    .TextAppearance_Material_Widget_ActionBar_Title
            )
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        // 상세화면 상단의 네비게이션 버튼의 <- 버튼 선택시 뒤로 가는 동작
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}