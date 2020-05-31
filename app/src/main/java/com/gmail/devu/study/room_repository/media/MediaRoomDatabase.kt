package com.gmail.devu.study.room_repository.media

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Media::class], version = 1)
abstract class MediaRoomDatabase : RoomDatabase() {
    private val TAG = this::class.java.simpleName

    abstract fun mediaDoa(): MediaDoa

    companion object {
        @Volatile
        private var INSTANCE: MediaRoomDatabase? = null

        // The app should only have one RoomDatabase instance, so it is best to implement
        // defensive code within the class to prevent more than one instance being created.
        fun getDatabase(context: Context, scope: CoroutineScope): MediaRoomDatabase {
            Log.v(TAG, "getDatabase()")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediaRoomDatabase::class.java,
                    "media_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(MediaRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance

                // return instance
                instance
            }
        }

        private class MediaRoomDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.v(TAG, "onCreate()")

                // Populate the database with sample data
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.mediaDoa())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.v(TAG, "onOpen()")
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                Log.v(TAG, "onDestructiveMigration()")
            }
        }

        fun populateDatabase(mediaDoa: MediaDoa) {
            for (i in 1..5) {
                val title = "Title-%d".format(i)
                val artist = "Artist-%d".format(i)
                mediaDoa.insert(Media(0, 0, title, artist))
            }

            // Title only
            for (i in 6..10) {
                val title = "Title-%d".format(i)
                mediaDoa.insert(Media(0, title))
            }
        }
    }
}
