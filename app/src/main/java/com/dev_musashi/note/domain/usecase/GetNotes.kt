package com.dev_musashi.note.domain.usecase

import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotes @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotes()
    }
}