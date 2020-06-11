package com.gmail.devu.study.room_repository.media

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MediaDoa {

    // NOTE: "... ORDER BY `orderIndex` ASC" does not work on Android Room
    // Do not use `xxx`
    @Query("SELECT * from media_table ORDER BY orderIndex ASC")
    fun getList(): LiveData<List<Media>>

    @Query("SELECT COUNT(*) from media_table")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(media: Media)

    @Query("DELETE FROM media_table")
    fun deleteAll()

    @Query("DELETE FROM media_table WHERE mediaId = :id")
    fun delete(id: Int)

    @Query("UPDATE media_table SET totalTrackCount = :totalCount")
    fun updateTotalTrackCountAll(totalCount: Int)

    @Query("UPDATE media_table SET orderIndex = :index WHERE mediaId = :id")
    fun updateOrder(id: Int, index: Int)

    @Query("UPDATE media_table SET orderIndex = (orderIndex - 1) WHERE orderIndex BETWEEN :fromIndex AND :toIndex")
    fun moveOrderForward(fromIndex: Int, toIndex: Int)

    @Query("UPDATE media_table SET orderIndex = (orderIndex - 1) WHERE orderIndex >= :fromIndex")
    fun moveOrderForwardAll(fromIndex: Int)

    @Query("UPDATE media_table SET orderIndex = (orderIndex + 1) WHERE orderIndex BETWEEN :fromIndex AND :toIndex")
    fun moveOrderBack(fromIndex: Int, toIndex: Int)

}