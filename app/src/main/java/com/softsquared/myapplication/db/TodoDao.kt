package com.softsquared.myapplication.db

import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo ")
    fun getAll(): List<Todo>

    //date를 string으로 표현하는 이유는 그날 껄 뽑아올려고 하는거지? string으로 넣을때 변환해서 넣어도 좋고 typeconverter를 쓰는 방법도 있고
    //long - date로 typeconverter만들어서 where date(day) = date(:date) 이 조건이 될진 모르지만 이런식으로 할 수도 있다는 것만 참고~
    @Query("SELECT * FROM Todo WHERE day = :date")
    fun getDayList(date: String): List<Todo>

    @Query("SELECT IFNULL(MAX(gid) + 1, 1) FROM Todo")
    fun getNewGid(): Long

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}