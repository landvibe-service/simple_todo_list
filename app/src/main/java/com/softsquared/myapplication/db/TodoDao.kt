package com.softsquared.myapplication.db

import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo ")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM Todo WHERE day = :date")
    fun getDayList(date: String): List<Todo>

    @Query("SELECT IFNULL(MAX(gid) + 1, 1) FROM Todo")
    fun getNewGid(): Long

    @Query("SELECT COUNT(*) FROM Todo WHERE gid = :gid")
    fun getMyGroupSize(gid: Long): Long

    @Query("DELETE FROM Todo WHERE gid = :gid")
    fun removeGroup(gid: Long)

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}