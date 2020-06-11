package com.gmail.devu.study.room_repository.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gmail.devu.study.room_repository.media.Media
import com.gmail.devu.study.room_repository.media.MediaRepository
import com.gmail.devu.study.room_repository.media.MediaRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = this::class.java.simpleName

    private val repository: MediaRepository

    val allMedias: LiveData<List<Media>>

    init {
        val mediaDoa = MediaRoomDatabase.getDatabase(application, viewModelScope).mediaDoa()
        repository = MediaRepository(mediaDoa)
        allMedias = repository.allMedias
    }

    fun insert(media: Media) = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, "insert(%s)".format(media.title))
        repository.insert(media)
    }

    fun removeAt(index: Int) = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, "removeAt(%d)".format(index))
        allMedias.value?.get(index)?.let { repository.delete(it) }
    }

    fun move(oldIndex: Int, newIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
        Log.v(TAG, "move(%d, %d)".format(oldIndex, newIndex))
        repository.move(oldIndex, newIndex)
    }
}
