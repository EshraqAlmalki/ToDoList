package com.example.todolist.dateBase

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*


@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo")
    fun getAllToDo():LiveData<List<ToDo>>

   @Query("SELECT * FROM todo WHERE id=(:id)")
   fun getToDo(id:UUID):LiveData<ToDo?>

   @Update
   fun updateToDo(todo:ToDo)

   @Insert
   fun addToDo(todo: ToDo)

   @Delete
   fun delToDo(todo: ToDo)


}