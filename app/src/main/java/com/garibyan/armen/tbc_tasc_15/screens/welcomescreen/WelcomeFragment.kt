package com.garibyan.armen.tbc_tasc_15.screens.welcomescreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.garibyan.armen.tbc_tasc_15.databinding.FragmentWelcomeBinding
import com.garibyan.armen.tbc_tasc_15.screens.BaseFragment
import kotlinx.coroutines.launch

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(
    FragmentWelcomeBinding::inflate
) {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoggedIn()
    }

    private fun onClickNavigation() = with(binding) {
        btnLogin.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }

        btnRegister.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment())
        }
    }

    private fun isLoggedIn() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataStoreRepository.getToken.collect {
                    when (it.isEmpty()) {
                        true -> {
                            onClickNavigation()
                        }
                        false -> {
                            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
                        }
                    }
                }
            }
        }
    }

}