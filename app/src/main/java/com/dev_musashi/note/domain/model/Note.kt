package com.dev_musashi.note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note (
    @PrimaryKey val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int
)
