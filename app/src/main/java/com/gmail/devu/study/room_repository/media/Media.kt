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
    var parentId: Int,
    var title: String,
    var artist: String?
) {
    constructor(parentId: Int, title: String, artist: String?) : this(0, parentId, title, artist)
    constructor(parentId: Int, title: String) : this(0, parentId, title, null)
}
