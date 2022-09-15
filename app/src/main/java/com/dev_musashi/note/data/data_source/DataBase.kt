package com.dev_musashi.note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev_musashi.note.domain.model.Note


@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    companion object{
        const val DB_NAME = "noteDB"
    }
    abstract val dao : Dao
}