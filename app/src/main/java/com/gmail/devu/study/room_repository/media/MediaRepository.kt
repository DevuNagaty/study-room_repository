package com.gmail.devu.study.room_repository.media

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import java.text.FieldPosition

class MediaRepository(private val mediaDoa: MediaDoa) {
    private val TAG = this::class.java.simpleName

    val allMedias: LiveData<List<Media>> = mediaDoa.getList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(media: Media) {
        Log.v(TAG, "insert(%s)".format(media.title))

        // The media has been added, so the total count increase
        val new_totalcount = mediaDoa.count() + 1
        mediaDoa.updateTotalTrackCountAll(new_totalcount)

        // Insert the media at the end of list
        media.totalTrackCount = new_totalcount
        media.orderIndex = new_totalcount - 1
        mediaDoa.insert(media)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(media: Media) {
        Log.v(TAG, "delete(%s)".format(media.title))

        // Delete the media
        mediaDoa.delete(media.id)

        // The media has been deleted, so the total count decrease
        mediaDoa.updateTotalTrackCountAll(mediaDoa.count())

        // Shift the index of the media after the deleted media
        mediaDoa.moveOrderForwardAll(media.orderIndex + 1)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun move(oldIndex: Int, newIndex: Int) {
        Log.v(TAG, "move(%d, %d)".format(oldIndex, newIndex))

        val media = allMedias.value?.get(oldIndex)
        if (media != null) {
            if (newIndex < oldIndex) {
                // Target media ｍove forward, so other medias move backward
                mediaDoa.moveOrderBack(newIndex, oldIndex - 1)
            } else {
                // Target media ｍove backward, so other medias move forward
                mediaDoa.moveOrderForward(oldIndex + 1, newIndex)
            }

            // Set new index
            mediaDoa.updateOrder(media.id, newIndex)
        }
    }
}