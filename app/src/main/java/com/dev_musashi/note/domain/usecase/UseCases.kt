package com.dev_musashi.note.domain.usecase

data class UseCases(
    val getNotes: GetNotes,
    val getNote: GetNote,
    val addNote: AddNote,
    val deleteNote: DeleteNote
)
