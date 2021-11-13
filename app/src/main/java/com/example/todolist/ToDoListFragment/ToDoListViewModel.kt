package com.example.todolist.ToDoListFragment

import androidx.lifecycle.ViewModel
import com.example.todolist.R
import com.example.todolist.dateBase.ToDo
import com.example.todolist.dateBase.ToDoRepository

class ToDoListViewModel:ViewModel() {


    val todoRepository=ToDoRepository.get()

    val LiveDataTodo = todoRepository.getAllToDo()


    fun delTodo(toDo: ToDo){
        todoRepository.delToDo(toDo)
    }

    val brown = R.color.brown
    val blue = R.color.blue
    val yellow = R.color.yellow


}