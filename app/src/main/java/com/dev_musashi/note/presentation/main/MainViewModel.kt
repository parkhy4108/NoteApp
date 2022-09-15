package com.dev_musashi.note.presentation.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_musashi.note.Screen
import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.usecase.AddNote
import com.dev_musashi.note.domain.usecase.DeleteNote
import com.dev_musashi.note.domain.usecase.GetNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNotes: GetNotes,
    private val addNote: AddNote,
    private val deleteNote: DeleteNote
) : ViewModel() {

    var state = mutableStateOf(MainState())
        private set

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        getNotesJob?.cancel()
        getNotesJob = getNotes()
            .onEach {
                val list = it.sortedByDescending { note-> note.timestamp }
                state.value = state.value.copy(notes = list)
            }
            .launchIn(viewModelScope)
    }

    fun addButtonClick(openScreen: (String) -> Unit) {
        openScreen(Screen.Note.route)
    }

    fun deleteButtonClick(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNote(note = note)
            recentlyDeletedNote = note
        }
    }

    fun noteClick(
        id: Int,
        openScreen: (String) -> Unit
    ){
        openScreen(Screen.Note.passId(id))
    }

    fun undoClick() {
        viewModelScope.launch(Dispatchers.IO) {
            addNote(recentlyDeletedNote!!)
            recentlyDeletedNote = null
        }
    }

}