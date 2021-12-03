package com.example.todoapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.databinding.FragmentDetailBinding
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.logMe
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class DetailFragment: Fragment() {

    private val viewModel by activityViewModels<ToDoViewModel>()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentDetailBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInfo()
        setUpBtnDelete()
        setUpBtnEdit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadInfo() = with(binding) {
        val todoComponent = mutableListOf<String>()
        args.todoDetail.let{
            //todoComponent.add("Id :    " + it.id.toString())
            todoComponent.add("Title :    " + it.title)
            todoComponent.add("Description :    " + it.description)
            todoComponent.add("Completed :    " + it.completed)
            todoComponent.add("Date :    " + it.date)
            todoComponent.add("Updated At :    " + it.updateAt)
        }

        lvDetail.adapter = this@DetailFragment.context?.let {
            ArrayAdapter( it, android.R.layout.simple_list_item_1, todoComponent)
        }
    }

    private fun setUpBtnDelete() = with(binding) {
        btnDelete.setOnClickListener {
            viewModel.delete(args.todoDetail).observe(viewLifecycleOwner){ state ->
                when (state) {
                    is ViewState.Loading -> {
                        (activity as? MainActivity)?.showProgress = true
                        btnDelete.isEnabled = false }
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

    private fun setUpBtnEdit() = with(binding) {
        btnEdit.setOnClickListener {
            val action = DetailFragmentDirections.goToEdit(args.todoDetail)
            findNavController().navigate(action)
        }
    }
}