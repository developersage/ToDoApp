package com.example.todoapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.databinding.FragmentEditBinding
import com.example.todoapp.model.ToDoRequest
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.logMe
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class EditFragment: Fragment() {
    private val viewModel by activityViewModels<ToDoViewModel>()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentEditBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setUpEditBtn()
        setUpCancelBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpView() = with(binding) {
        etTitle.setText(args.todoDetail.title)
        etDescription.setText(args.todoDetail.description)
        swCompleted.isChecked = args.todoDetail.completed.toBoolean()
    }

    private fun setUpEditBtn() = with(binding) {
        btnEdit.setOnClickListener {
            val request = ToDoRequest(
                etTitle.text.toString(), etDescription.text.toString(), swCompleted.isChecked)
            viewModel.edit(args.todoDetail, request).observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ViewState.Loading -> {
                        (activity as? MainActivity)?.showProgress = true
                        btnEdit.isEnabled = false }
                    is ViewState.Error -> {
                        (activity as? MainActivity)?.showProgress = false
                        "Edit Error".logMe() }
                    is ViewState.Success -> {
                        (activity as? MainActivity)?.showProgress = false
                        findNavController().navigateUp()
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun setUpCancelBtn() = with(binding) {
        btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}