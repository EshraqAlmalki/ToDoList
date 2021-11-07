package com.example.todolist.dateBase

import android.app.Application

class ToDoApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        ToDoRepository.initialize(this)
    }
}