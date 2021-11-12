package com.example.todolist.ToDoListFragment

import androidx.lifecycle.ViewModel
import com.example.todolist.dateBase.ToDo
import com.example.todolist.dateBase.ToDoRepository

class ToDoListViewModel:ViewModel() {


    val todoRepository=ToDoRepository.get()

    val LiveDataTodo = todoRepository.getAllToDo()


    fun delTodo(toDo: ToDo){
        todoRepository.delToDo(toDo)
    }


}