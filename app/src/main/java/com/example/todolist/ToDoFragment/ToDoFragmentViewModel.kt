package com.example.todolist.ToDoFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todolist.dateBase.ToDo
import com.example.todolist.dateBase.ToDoRepository
import java.util.*

class ToDoFragmentViewModel:ViewModel() {

        private val toDoRepository=ToDoRepository.get()
    private val todoIdLiveData = MutableLiveData<UUID>()
    var todoLiveData:LiveData<ToDo?> =
    Transformations.switchMap(todoIdLiveData){
        toDoRepository.getToDo(it)

    }

    fun loadToDo(todoId:UUID){
        todoIdLiveData.value=todoId
    }

    fun saveUpdate(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

    fun addTodo(toDo: ToDo){
        toDoRepository.addToDo(toDo)
    }







}