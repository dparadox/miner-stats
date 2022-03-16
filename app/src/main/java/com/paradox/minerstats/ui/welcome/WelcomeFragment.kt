package com.paradox.minerstats.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paradox.minerstats.BR
import com.paradox.minerstats.R
import com.paradox.minerstats.databinding.FragmentWelcomeBinding
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation.ShowError
import com.paradox.minerstats.ui.welcome.navigation.WelcomeNavigation.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var binding: FragmentWelcomeBinding? = null
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
        }

        observeEvents()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchExistingLocalWallet()
    }

    private fun observeEvents() {
        viewModel.navigationEvents.observe(viewLifecycleOwner) {
            when (it) {
                is ShowError -> Toast.makeText(context, it.message, LENGTH_LONG)
                    .show()
                is Success -> findNavController().navigate(R.id.navigation_home)
            }
        }
    }
}