package com.example.todolist.dateBase

import androidx.room.Entity
import java.util.*

@Entity
data class ToDo(
    @PrimaryKye val id:UUID=UUID.randomUUID(),
    var title:String="",
    var description:String="",
    var date: Date =Date(),
    var done:Boolean=false

)