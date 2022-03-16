package com.paradox.minerstats.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paradox.minerstats.R
import com.paradox.minerstats.BR
import com.paradox.minerstats.databinding.FragmentSettingsBinding
import com.paradox.minerstats.ui.settings.navigation.SettingsNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
        }

        observeEvents()

        return binding!!.root
    }

    private fun observeEvents() {
        viewModel.navigationEvents.observe(viewLifecycleOwner) {
            when (it) {
                is SettingsNavigation.Success -> findNavController().popBackStack(
                    R.id.navigation_welcome,
                    true
                )
            }
        }
    }
}