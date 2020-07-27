package com.softsquared.myapplication.db

import androidx.room.*
import java.util.*

@Entity
data class Todo(
    var contents: String,
    var clear: Boolean,
    var day: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}


