package com.example.todoapp.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.model.UserInfo
import com.example.todoapp.util.ViewState
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class LoginFragment: Fragment(){

    private val viewModel by activityViewModels<ToDoViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBtnLogin()
        setUpBtnRegister()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBtnLogin() = with(binding) {
        btnLogin.setOnClickListener {
            val userInfo = UserInfo(etUsername.text.toString(), etPassword.text.toString())
            viewModel.logIn(userInfo).observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ViewState.Loading -> {
                        btnLogin.isEnabled = false
                        (activity as? MainActivity)?.showProgress = true
                    }
                    is ViewState.Error -> { (activity as? MainActivity)?.showProgress = false }
                    is ViewState.Success -> { (activity as? MainActivity)?.showProgress = false }
                }
            }
        }
    }

    private fun setUpBtnRegister() = with(binding) {
        btnRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.goToRegister())
        }
    }

}