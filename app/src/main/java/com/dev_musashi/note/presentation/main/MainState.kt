package com.dev_musashi.note.presentation.main

import com.dev_musashi.note.domain.model.Note

data class MainState (
    val notes: List<Note> = emptyList(),
    val animatedVisibleBoolean: Boolean = false
)