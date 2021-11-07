package com.example.todolist.dateBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ToDo::class],version = 1)
@TypeConverters(ToDoTypeConverters::class)
abstract class ToDoDateBase: RoomDatabase() {
    abstract fun todoDao():ToDoDao
}