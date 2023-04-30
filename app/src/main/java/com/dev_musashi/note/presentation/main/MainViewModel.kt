package com.dev_young.note.presentation.notesList

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_young.note.Screen
import com.dev_young.note.domain.model.Note
import com.dev_young.note.domain.usecase.AddNote
import com.dev_young.note.domain.usecase.DeleteNote
import com.dev_young.note.domain.usecase.GetNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
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

    var recentlyDeletedNote: Note? = null

//    init {
//        getAllNotes()
//    }

    fun getAllNotes() {
        getNotesFlow().launchIn(viewModelScope)
    }

    private fun getNotesFlow(): Flow<List<Note>> = getNotes().onEach {
        val list = it.sortedByDescending { note -> note.timestamp }
        state.value = state.value.copy(notes = list)
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

    fun undoClick() {
        viewModelScope.launch(Dispatchers.IO) {
            addNote(recentlyDeletedNote!!)
            recentlyDeletedNote = null
        }
    }

    fun noteClick(
        id: Int,
        openScreen: (String) -> Unit
    ) {
        openScreen(Screen.Note.passId(id))
    }
}
