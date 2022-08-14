package com.garibyan.armen.tbc_tasc_15.screens.homescreen

import androidx.fragment.app.viewModels
import com.garibyan.armen.tbc_tasc_15.databinding.FragmentHomeBinding
import com.garibyan.armen.tbc_tasc_15.screens.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val viewModel: HomeViewModel by viewModels()

}