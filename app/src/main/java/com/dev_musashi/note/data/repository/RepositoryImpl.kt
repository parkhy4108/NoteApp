package com.dev_musashi.note.data.repository

import com.dev_musashi.note.data.data_source.Dao
import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: Dao
    ) : Repository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNote(id: Int): Note? {
        return dao.getNote(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note = note)
    }

    override suspend fun deleteNote(note: Note) {
        return dao.deleteNote(note = note)
    }
}