package com.softsquared.myapplication.db

import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo ")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM Todo WHERE day = :date")
    fun getDayList(date: String): List<Todo>

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}