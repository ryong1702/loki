package com.application.lokisystem.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.Dialog
import com.application.lokisystem.LoadingDialog
import com.application.lokisystem.adapter.CategoryAdapter
import com.application.lokisystem.api.LokiSystemApi
import com.application.lokisystem.databinding.FragmentCategoryBinding
import kotlinx.coroutines.*

class Category : Fragment() {
    lateinit var binding: FragmentCategoryBinding
    private var backKeyPressedTime : Long = 0
    private lateinit var callback: OnBackPressedCallback
    private var job = Job()
    private val categoryAdapter by lazy{
        CategoryAdapter()
    }

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val dialog = LoadingDialog(binding.root.context)
        dialog.setCancelable(false)
        dialog.show()
        retrofitWork(dialog)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                    backKeyPressedTime = System.currentTimeMillis()
                    Toast.makeText(view?.context, "한번 더 클릭하면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun retrofitWork(dialog: LoadingDialog) {
        CoroutineScope(Dispatchers.IO + job).launch {
            try {
                val categoryDataResponse = LokiSystemApi.apiService.getThemeData()
                withContext(Dispatchers.Main) {
                    if (categoryDataResponse.isSuccessful) {
                        val recyclerView: RecyclerView = binding.rvCategory
                        recyclerView.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        recyclerView.setHasFixedSize(true)
                        recyclerView.adapter = categoryAdapter
                        val result = categoryDataResponse.body()?.themes
                        result?.let {
                            categoryAdapter.submitList(it)
                        }
                    } else {
                        Log.d("categoryDataService", categoryDataResponse.code().toString())
                    }
                }
            } catch (exception: Exception) {
                Log.d("exception", exception.toString())
                Dialog().show(childFragmentManager, "tag")
            } finally {
                dialog.dismiss()
                job.cancel()
            }
        }
    }
}