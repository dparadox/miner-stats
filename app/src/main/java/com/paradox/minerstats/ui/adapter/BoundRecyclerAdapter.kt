package com.paradox.minerstats.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.paradox.minerstats.BR

/*
    Generic RecyclerView to Speed-up the implementation
*/
class BoundRecyclerAdapter<T>(
    private val viewModel: ViewModel,
    private val layoutResolver: (T) -> Int
) : RecyclerView.Adapter<BoundRecyclerAdapter<T>.BoundViewHolder>() {

    var data: List<T> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoundViewHolder =
        BoundViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

    override fun onBindViewHolder(holder: BoundViewHolder, position: Int) {
        holder.binding.also {
            it.setVariable(BR.entity, data[position])
            it.setVariable(BR.vm, viewModel)
            fadeAnimation(it.root)
        }.executePendingBindings()
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = layoutResolver(data[position])

    private fun fadeAnimation(view: View) {
        AlphaAnimation(0.0f, 1.0f).apply {
            duration = 800
        }.also {
            view.startAnimation(it)
        }
    }

    inner class BoundViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val binding: ViewDataBinding = DataBindingUtil.bind(root)!!
    }
}

