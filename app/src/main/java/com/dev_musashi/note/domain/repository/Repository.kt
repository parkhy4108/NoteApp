package com.dev_musashi.note.domain.repository

import com.dev_musashi.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getNotes() : Flow<List<Note>>
    suspend fun getNote(id: Int) : Note
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
}