package com.dev_musashi.note.domain.usecase

import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.repository.Repository
import javax.inject.Inject

class GetNote @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNote(id)
    }
}