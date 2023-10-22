package com.application.lokisystem.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.*
import com.application.lokisystem.adapter.SupplyAdapter
import com.application.lokisystem.api.LokiSystemApi
import com.application.lokisystem.databinding.FragmentModelBinding
import kotlinx.coroutines.*
import okhttp3.*


/**
 * A simple [Fragment] subclass.
 */
class Model : Fragment() {
    lateinit var binding: FragmentModelBinding
    lateinit var callback: OnBackPressedCallback
    private var backKeyPressedTime : Long = 0
    private var job = Job()
    private val supplyAdapter by lazy{
        SupplyAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModelBinding.inflate(inflater, container, false)
        val dialog = LoadingDialog(binding.root.context)
        dialog.setCancelable(false)
        dialog.show()
        lokiShowWebView()
        retrofitWork(dialog)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun lokiShowWebView(): Boolean {
        val webViewUrl = BuildConfig.URL_WEBVIEW
        val webView: WebView = binding.root.findViewById(R.id.webView_chart)

        webView.post(Runnable {
            webView.webViewClient = object: WebViewClient() {
                // 웹뷰 에러시
                override fun onReceivedError(view: WebView?, request: WebResourceRequest, error: WebResourceError?) {
                    if (request.isForMainFrame) {
                        binding.webViewEmpty.root.visibility = VISIBLE
                    }
                }
            }
            webView.settings.apply {
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_NO_CACHE

            /**
             * This request has been blocked; the content must be served over HTTPS
             * https 에서 이미지가 표시 안되는 오류를 해결하기 위한 처리
             */
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webView.loadUrl(webViewUrl)
        })
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                    backKeyPressedTime = System.currentTimeMillis()
                    Toast.makeText(view?.context, "한번 더 클릭하면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("SetTextI18n")
     private fun retrofitWork(dialog: LoadingDialog) {
        CoroutineScope(Dispatchers.IO + job).launch {
            try {
                val marketStatusResponse = LokiSystemApi.apiService.getMarketStatusData()
                val supplyDataResponse = LokiSystemApi.apiService.getSupplyData()

                withContext(Dispatchers.Main) {
                    if (marketStatusResponse.isSuccessful) {
                        val beforeRatioTextView = binding.beforeRatio
                        val marketStatusTextView = binding.marketStatusText
                        binding.market.text = marketStatusResponse.body()?.market.toString()
                        binding.closePrice.text = marketStatusResponse.body()?.closePrice.toString()
                        beforeRatioTextView.text = marketStatusResponse.body()?.beforeRatio.toString()

                        when {
                            marketStatusResponse.body()?.arrow!! == 1 -> beforeRatioTextView.text = marketStatusResponse.body()!!.beforeRatio.toString() + " ▼"
                            marketStatusResponse.body()!!.arrow == 2 -> beforeRatioTextView.text = marketStatusResponse.body()!!.beforeRatio.toString()
                            else -> beforeRatioTextView.text = marketStatusResponse.body()!!.beforeRatio.toString() + " ▲"
                        }

                        if(beforeRatioTextView.text[0] == '+'){
                            beforeRatioTextView.setTextColor(Color.parseColor("red"));
                        } else if(beforeRatioTextView.text[0] == '-') {
                            beforeRatioTextView.setTextColor(Color.parseColor("blue"));
                        }

                        marketStatusTextView.text = marketStatusResponse.body()!!.marketStatus.toString()
                        if(marketStatusTextView.text[0] == '1'){
                            marketStatusTextView.text = getString(R.string.ic_cloud)
                            binding.marketStatus.setImageResource(R.drawable.ic_cloud)
                            binding.marketStatus.maxWidth = 550
                        } else if(marketStatusTextView.text[0] == '2') {
                            marketStatusTextView.text = getString(R.string.ic_rain)
                            binding.marketStatus.setImageResource(R.drawable.ic_rain)
                        } else {
                            marketStatusTextView.text = getString(R.string.ic_sun)
                            binding.marketStatus.setImageResource(R.drawable.ic_sun)
                        }
                        binding.standardDate.text = getString(R.string.standard) + marketStatusResponse.body()!!.standardDate
                    } else {
                        Log.d("marketStatusResponse", marketStatusResponse.code().toString())
                        job.cancel()
                    }

                    if (supplyDataResponse.isSuccessful) {
                        val recyclerView: RecyclerView = binding.rvTop5
                        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        recyclerView.setHasFixedSize(true)
                        recyclerView.adapter = supplyAdapter
                        recyclerView.addItemDecoration(CustomDecoration())
                        val result = supplyDataResponse.body()?.top5
                        result?.let {
                            supplyAdapter.submitList(it)
                        }
                    } else {
                        Log.d("supplyDataResponse", supplyDataResponse.code().toString())
                        job.cancel()
                    }
                }
            } catch (exception: Exception) {
                Log.d("model exception", exception.toString())
                Dialog().show(childFragmentManager, "tag")
            } finally {
                dialog.dismiss()
                job.cancel()

            }
        }
    }
}