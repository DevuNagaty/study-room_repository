package com.gmail.devu.study.room_repository.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NonNls

@Entity(tableName = "media_table")
data class Media(
    @PrimaryKey(autoGenerate = true)
    @NonNls
    @ColumnInfo(name = "mediaId")
    var id: Int,
    var title: String,
    var artist: String?,
    var trackNumber: Int,
    var totalTrackCount: Int,
    // `orderIndex` is start from 0
    var orderIndex: Int
) {
    constructor(
        title: String,
        artist: String?,
        trackNumber: Int,
        totalTrackCount: Int,
        orderIndex: Int
    ) : this(0, title, artist, trackNumber, totalTrackCount, orderIndex)

    // No artist
    constructor(
        title: String,
        trackNumber: Int,
        totalTrackCount: Int,
        orderIndex: Int
    ) : this(0, title, null, trackNumber, totalTrackCount, orderIndex)
}
