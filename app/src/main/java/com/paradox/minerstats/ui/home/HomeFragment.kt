package com.paradox.minerstats.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.paradox.minerstats.BR
import com.paradox.minerstats.R
import com.paradox.minerstats.databinding.FragmentHomeBinding
import com.paradox.minerstats.model.dto.Worker
import com.paradox.minerstats.ui.adapter.BoundRecyclerAdapter
import com.paradox.minerstats.ui.home.navigation.HomeNavigation.ShowError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var boundAdapter: BoundRecyclerAdapter<Worker>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@HomeFragment
            setVariable(BR.vm, viewModel)
        }

        setWorkersAdapter()
        setSwipeRefreshListener()
        observeEvents()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
    }

    private fun setWorkersAdapter() {
        boundAdapter = BoundRecyclerAdapter(viewModel) { R.layout.item_view_worker }

        binding?.recyclerView?.apply {
            adapter = boundAdapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
    }

    private fun setSwipeRefreshListener() {
        binding?.swipeRefreshView?.setOnRefreshListener {
            fetchData()
        }
    }

    private fun observeEvents() {
        with(viewLifecycleOwner) {
            viewModel.workers.observe(this) {
                boundAdapter.data = it
                binding?.swipeRefreshView?.isRefreshing = false
            }

            viewModel.navigationEvents.observe(this) {
                when (it) {
                    is ShowError -> Toast.makeText(context, it.message, LENGTH_LONG).show()
                }
            }

            viewModel.inProgress.observe(this) {
                binding?.swipeRefreshView?.isRefreshing = it
            }
        }
    }

    private fun fetchData() {
        viewModel.fetchWorkers()
        viewModel.fetchStatistics()
    }
}