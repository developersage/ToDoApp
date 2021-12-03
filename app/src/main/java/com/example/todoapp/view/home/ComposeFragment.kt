package com.example.todoapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentComposeBinding
import com.example.todoapp.model.ToDoRequest
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.display
import com.example.todoapp.util.logMe
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class ComposeFragment: Fragment() {

    private val viewModel by activityViewModels<ToDoViewModel>()
    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentComposeBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpComposeBtn()
        setUpBackBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpComposeBtn() = with(binding) {
        btnCompose.setOnClickListener {
            val request = ToDoRequest(
                etTitle.text.toString(), etDescription.text.toString(), swCompleted.isChecked)
            viewModel.createNew(request).observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ViewState.Loading -> {
                        (activity as? MainActivity)?.showProgress = true
                        btnCompose.isEnabled = false }
                    is ViewState.Error -> {
                        (activity as? MainActivity)?.showProgress = false
                        state.errorMsg?.logMe() }
                    is ViewState.Success -> {
                        (activity as? MainActivity)?.showProgress = false
                        findNavController().navigateUp() }
                }
            }
        }
    }
    private fun setUpBackBtn() = with(binding) {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}