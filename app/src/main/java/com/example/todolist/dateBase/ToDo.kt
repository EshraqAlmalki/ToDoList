package com.example.todolist.dateBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ToDo(
    @PrimaryKey val id:UUID=UUID.randomUUID(),
    var title:String="",
    var description:String="",
    var duoDate: Date? =null,
    var done:Boolean=false,
    var category: String="",
    var date:Date = Date()

)