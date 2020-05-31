package com.gmail.devu.study.room_repository.media

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MediaRepository(private val mediaDoa: MediaDoa) {

    val allMedias: LiveData<List<Media>> = mediaDoa.getMediaList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(media: Media) {
        mediaDoa.insert(media)
    }
}