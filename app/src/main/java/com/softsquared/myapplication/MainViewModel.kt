package com.softsquared.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Todo

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val db = Room.databaseBuilder(application,
        AppDatabase::class.java, "todo.db")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
    fun getAll(): List<Todo>? {
        return db.todoDao().getAll()
    }
    fun insert(todo: Todo){
        db.todoDao().insert(todo)
    }
    fun update(todo: Todo){
        db.todoDao().update(todo)
    }
    fun delete(todo: Todo){
        db.todoDao().delete(todo)
    }
    fun getDayList(date: String): List<Todo> {
        return db.todoDao().getDayList(date)
    }
    fun getNewGid(): Long{
        return db.todoDao().getNewGid()
    }
    fun getMyGroupSize(gid: Long): Long{
        return db.todoDao().getMyGroupSize(gid)
    }
    fun removeGroup(gid: Long){
        db.todoDao().removeGroup(gid)
    }
}