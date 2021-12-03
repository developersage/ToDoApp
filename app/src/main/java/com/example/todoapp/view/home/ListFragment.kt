package com.example.todoapp.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.SwipeToDeleteCallback
import com.example.todoapp.adapter.ToDoAdapter
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.model.ToDoData
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.logMe
import com.example.todoapp.view.MainActivity
import com.example.todoapp.viewmodel.ToDoViewModel

class ListFragment: Fragment() {

    private val viewModel by activityViewModels<ToDoViewModel>()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentListBinding.inflate(
        inflater, container, false
    ).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpSwipeDelete()
        observeLocalDatabase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        root.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            (activity as? MainActivity)?.showFab = oldScrollY > scrollY
        }
    }

    private fun itemSelected(item: ToDoData) = with(findNavController()) {
        val action = ListFragmentDirections.goToDetail(item)
        navigate(action)
    }

    private fun observeLocalDatabase() = with(viewModel) {
        todos.observe(viewLifecycleOwner){ todoList ->
            binding.rcTodo.adapter = ToDoAdapter(todoList, ::itemSelected)
        }
    }

    private fun setUpSwipeDelete() = with(viewModel) {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = todos.value?.get(viewHolder.adapterPosition)
                delete(item!!).observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is ViewState.Loading -> {
                            (activity as? MainActivity)?.showProgress = true }
                        is ViewState.Error -> {
                            (activity as? MainActivity)?.showProgress = false
                            state.errorMsg?.logMe() }
                        is ViewState.Success -> {
                            (activity as? MainActivity)?.showProgress = false }
                    }
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rcTodo)
    }
}