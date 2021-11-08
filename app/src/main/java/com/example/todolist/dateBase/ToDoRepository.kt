package com.example.todolist.dateBase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "todo-database"
class ToDoRepository private constructor(context: Context){

    private val dateBase:ToDoDateBase= Room.databaseBuilder(
        context.applicationContext,ToDoDateBase::class.java,
        DATABASE_NAME
    ).build()

    private val todoDao = dateBase.todoDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getAllToDo():LiveData<List<ToDo>> = todoDao.getAllToDo()

    fun getToDo(id: UUID):LiveData<ToDo?>{
        return todoDao.getToDo(id)
    }

    fun updateToDo(todo:ToDo) {
        executor.execute {
            todoDao.updateToDo(todo)
        }
    }


    fun addToDo(todo: ToDo){
        executor.execute {
            todoDao.addToDo(todo)
        }
    }

    fun delToDo(todo: ToDo){
        executor.execute {
            todoDao.delToDo(todo)
        }
    }

    fun getCat(category: String){
        executor.execute {
            todoDao.getCat(category)
        }
    }

    companion object{
        var INSTANCE :ToDoRepository?=null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE= ToDoRepository(context)
            }
        }

        fun get():ToDoRepository{
            return INSTANCE?: throw IllegalStateException("ToDoRepository must be initialized")
        }
    }










}