package com.example.todoapp.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.logMe
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class ProfileFragment: Fragment() {
    private val viewModel by viewModels<ToDoViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentProfileBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBtnSync()
        setUpBtnLogout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBtnLogout() = with(binding) {
        btnLogout.setOnClickListener {
            viewModel.logOut()
        }
    }

    private fun setUpBtnSync() = with(binding) {
        btnSync.setOnClickListener {
            viewModel.fetchAndLoadAll().observe(viewLifecycleOwner) { state ->
                when (state){
                    is ViewState.Loading -> {
                        (activity as? MainActivity)?.showProgress = true
                        btnSync.isEnabled = false }
                    is ViewState.Error -> {
                        state.errorMsg?.logMe()
                        (activity as? MainActivity)?.showProgress = false
                    }
                    is ViewState.Success -> {
                        (activity as? MainActivity)?.showProgress = false
                        btnSync.isEnabled = true
                    }
                }
            }
        }
    }
}