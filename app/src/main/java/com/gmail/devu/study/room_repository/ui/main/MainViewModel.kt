package com.gmail.devu.study.room_repository.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gmail.devu.study.room_repository.media.Media
import com.gmail.devu.study.room_repository.media.MediaRepository
import com.gmail.devu.study.room_repository.media.MediaRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MediaRepository

    val allMedias: LiveData<List<Media>>

    init {
        val mediaDoa = MediaRoomDatabase.getDatabase(application, viewModelScope).mediaDoa()
        repository = MediaRepository(mediaDoa)
        allMedias = repository.allMedias
    }

    fun insert(media: Media) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(media)
    }
}
