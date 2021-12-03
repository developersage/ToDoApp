package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.model.ToDoData
import com.example.todoapp.util.logMe

class ToDoAdapter(
    private val toDoList: List<ToDoData>,
    private val toDoSelected: (ToDoData) -> Unit
): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): ToDoViewHolder = ToDoViewHolder.getInstance(parent).apply {
        itemView.setOnClickListener { toDoSelected(toDoList[adapterPosition]) }
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.loadBasicToDo(toDoList[position])
    }

    override fun getItemCount(): Int = toDoList.size

    class ToDoViewHolder(
        private val binding: TodoItemBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun loadBasicToDo(toDoBody: ToDoData) = with(binding) {
            tvTitle.text = toDoBody.title
            tvDescription.text = toDoBody.description
            if (toDoBody.completed.toBoolean()){
                ivCompleted.setImageResource(R.drawable.ic_check)
            }else{
                ivCompleted.setImageResource(0)
            }
        }

        companion object{
            fun getInstance(parent: ViewGroup) = TodoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).run { ToDoViewHolder(this) }
        }
    }

}