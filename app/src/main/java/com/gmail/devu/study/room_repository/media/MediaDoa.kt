package com.gmail.devu.study.room_repository.media

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MediaDoa {

    @Query("SELECT * from media_table")
    fun getMediaList(): LiveData<List<Media>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(media: Media)

    @Query("DELETE FROM media_table")
    fun deleteAll()
}