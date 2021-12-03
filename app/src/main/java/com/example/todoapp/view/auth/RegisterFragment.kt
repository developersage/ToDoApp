package com.example.todoapp.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentRegisterBinding
import com.example.todoapp.model.UserInfo
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.isEmailValid
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class RegisterFragment: Fragment() {

    private val viewModel by activityViewModels<ToDoViewModel>()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentRegisterBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBtnRegister()
        setUpBtnBack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBtnRegister() = with(binding){
        btnRegister.setOnClickListener {
            if (etEmail.text.toString().isEmailValid()){
                val userInfo = UserInfo(
                    etUsername.text.toString(), etPassword.text.toString(), etEmail.text.toString()
                )
                viewModel.register(userInfo).observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is ViewState.Loading -> {
                            (activity as? MainActivity)?.showProgress = true
                            btnRegister.isEnabled = false }
                        is ViewState.Error -> { (activity as? MainActivity)?.showProgress = false }
                        is ViewState.Success -> { (activity as? MainActivity)?.showProgress = false }
                    }
                }
            }else{
                etEmail.error = "Please Enter Valid Email."
                Toast.makeText(requireActivity().application, "Invalid Email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpBtnBack() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.goToLogin())
        }
    }

}