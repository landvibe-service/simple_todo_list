package com.softsquared.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    var contents: String,
    var clear: Boolean,
    var day: String,
    var gid: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

//id도 위에 선언하는게 좋을 것 같은데~