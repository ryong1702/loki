package com.application.lokisystem.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.lokisystem.CustomDecoration
import com.application.lokisystem.Dialog
import com.application.lokisystem.LoadingDialog
import com.application.lokisystem.R
import com.application.lokisystem.adapter.SearchAdapter
import com.application.lokisystem.adapter.SearchTop5
import com.application.lokisystem.api.LokiSystemApi
import com.application.lokisystem.databinding.FragmentSearchBinding
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 */
class Search : Fragment() {
    private var backKeyPressedTime : Long = 0
    private lateinit var binding: FragmentSearchBinding
    private lateinit var callback: OnBackPressedCallback
    private var job = Job()

    private val searchTop5 by lazy{
        SearchTop5()
    }
    private val searchAdapter by lazy{
        SearchAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
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
                val supplyDataResponse = LokiSystemApi.apiService.getSupplyData()
                val candidatesResponse = LokiSystemApi.apiService.getCandidateData()
                val recyclerViewTop5: RecyclerView = binding.rvTop5
                val recyclerViewSearch: RecyclerView = binding.rvSearch
                val textViewSearchTop5: TextView = binding.tvSearchTop5
                val resultTop5 = supplyDataResponse.body()?.top5
                val resultSearch = candidatesResponse.body()?.candidates
                withContext(Dispatchers.Main) {
                    if (supplyDataResponse.isSuccessful) {
                        val searchView: SearchView = binding.svSearch
                        searchView.isVisible = true
                        textViewSearchTop5.text = binding.root.resources.getString(R.string.top5)
                        recyclerViewTop5.isVisible = true
                        recycler(recyclerViewTop5)
                        resultTop5?.let {
                            searchTop5.submitList(it)
                        }
                    } else {
                        Log.d("supplyDataResponse", supplyDataResponse.code().toString())
                    }

                    if (candidatesResponse.isSuccessful) {
                        recycler(recyclerViewSearch)
                        resultSearch?.let {
                            searchAdapter.submitList(it)
                        }

                        binding.svSearch.setOnQueryTextListener(object :
                            SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                binding.svSearch.clearFocus()
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                val searchText = newText!!.trim()
                                if (searchText.isNotEmpty()) {
                                    val resultNameFilter =
                                        resultSearch?.filter { s -> s.name!!.contains(searchText) }
                                    val resultCodeFilter =
                                        resultSearch?.filter { s -> s.code!!.contains(searchText) }
                                    if (resultNameFilter!!.isEmpty() && resultCodeFilter!!.isEmpty()) {
                                        recyclerViewSearch.isVisible = false
                                        textViewSearchTop5.isVisible = false
                                        recyclerViewTop5.isVisible = false
                                    } else {
                                        if (resultNameFilter.isEmpty()) {
                                            searchAdapter.submitList(resultCodeFilter)
                                        } else {
                                            searchAdapter.submitList(resultNameFilter)
                                        }
                                        recyclerViewSearch.isVisible = true
                                        textViewSearchTop5.isVisible = false
                                        recyclerViewTop5.isVisible = false
                                    }
                                } else {
                                    recyclerViewSearch.isVisible = false
                                    textViewSearchTop5.isVisible = true
                                    recyclerViewTop5.isVisible = true
                                    retrofitWork(dialog)
                                }
                                return false
                            }
                        })
                    } else {
                        Log.d("candidatesResponse", candidatesResponse.code().toString())
                    }
                }
            } catch (exception : Exception) {
                Log.d("search exception", exception.toString())
                Dialog().show(childFragmentManager, "tag")
            } finally {

                dialog.dismiss()
                job.cancel()
            }
        }
    }

    private fun recycler(recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(false)
        recyclerView.addItemDecoration(CustomDecoration())
        if (recyclerView == recyclerView.findViewById(R.id.rv_top5)){
            recyclerView.adapter = searchTop5
        } else {
            recyclerView.adapter = searchAdapter
        }
    }
}