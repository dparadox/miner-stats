package com.paradox.minerstats.ui.payout

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.paradox.minerstats.BR
import com.paradox.minerstats.R
import com.paradox.minerstats.databinding.FragmentPayoutBinding
import com.paradox.minerstats.model.dto.Payout
import com.paradox.minerstats.ui.adapter.BoundRecyclerAdapter
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation.ShowError
import com.paradox.minerstats.ui.payout.navigation.PayoutNavigation.ShowPayoutDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayoutFragment : Fragment() {

    private var binding: FragmentPayoutBinding? = null
    private val viewModel: PayoutViewModel by viewModels()
    private lateinit var boundAdapter: BoundRecyclerAdapter<Payout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayoutBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
        }

        setPayoutsAdapter()
        setSwipeRefreshListener()
        observeEvents()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPayouts()
    }

    private fun setSwipeRefreshListener() {
        binding?.swipeRefreshView?.setOnRefreshListener {
            viewModel.fetchPayouts()
        }
    }

    private fun setPayoutsAdapter() {
        boundAdapter = BoundRecyclerAdapter(viewModel) { R.layout.item_view_payout }

        binding?.recyclerView?.apply {
            adapter = boundAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun observeEvents() {
        with(viewLifecycleOwner) {
            viewModel.payouts.observe(this) {
                boundAdapter.data = it
                binding?.swipeRefreshView?.isRefreshing = false
            }

            viewModel.navigationEvents.observe(this) {
                when (it) {
                    is ShowError -> Toast.makeText(context, it.message, LENGTH_LONG).show()
                    is ShowPayoutDetail -> {
                        val intent = Intent(ACTION_VIEW, Uri.parse(it.url))
                        startActivity(intent)
                    }
                }
            }

            viewModel.inProgress.observe(this) {
                binding?.swipeRefreshView?.isRefreshing = it
            }
        }
    }
}