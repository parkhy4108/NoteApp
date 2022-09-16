package com.dev_musashi.note.presentation.add

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.dev_musashi.note.ui.theme.Color0

data class NoteState (
    val title: String = "",
    val content : String = "",
    val color: Int = Color(0xFF1B1B1A).toArgb(),
    val titleHint: Boolean = true,
    val contentHint: Boolean = true,
    val colorSection: Boolean = true
)