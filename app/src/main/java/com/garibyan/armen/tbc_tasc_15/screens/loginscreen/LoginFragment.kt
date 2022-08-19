package com.garibyan.armen.tbc_tasc_15.screens.loginscreen

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.garibyan.armen.tbc_tasc_15.R
import com.garibyan.armen.tbc_tasc_15.databinding.FragmentLoginBinding
import com.garibyan.armen.tbc_tasc_15.network.Resource
import com.garibyan.armen.tbc_tasc_15.screens.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    FragmentLoginBinding::inflate, LoginViewModel::class.java
) {

    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
    }

    private fun onClickListeners() = with(binding) {
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            if (isValidInput(email, password)) {
                collectors()
                viewModel.login(email, password)
            }
        }
        btnPasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            passwordVisibility()
        }
    }

    private fun collectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collectLatest {
                    when (it) {
                        is Resource.Success -> {
                            dataStoreRepository.saveToken(it.value.token.toString())
                            successfulState()
                        }
                        is Resource.Error -> {
                            errorState(it.isNetworkError!!)
                        }
                        is Resource.Loading -> {
                            loadingState()
                        }
                    }
                }
            }
        }
    }

    private fun successfulState() {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.successfully_login),
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    private fun errorState(isNetworkError: Boolean) {
        binding.progressBar.visibility = View.GONE
        if (isNetworkError) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.network_error),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.wrong_credentials),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun passwordVisibility() {
        if (isPasswordVisible) {
            binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.btnPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_on_24)
        } else {
            binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.btnPasswordVisibility.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
    }

    //Validation
    private fun isValidInput(email: String, password: String): Boolean {
        var isValid = false
        when (false) {
            !isEmptyEmail(email) -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.empty_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
            isValidEmail(email) -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.incorrect_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
            !isEmptyPassword(password) -> {
                Toast.makeText(
                    requireContext(),
                    requireContext().getText(R.string.empty_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> isValid = true
        }
        return isValid
    }

    private fun isEmptyEmail(email: String) = email.isEmpty()
    private fun isEmptyPassword(password: String) = password.isEmpty()
    private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

}