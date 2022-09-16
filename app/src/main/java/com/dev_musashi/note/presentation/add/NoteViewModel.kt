package com.dev_musashi.note.presentation.add

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.usecase.AddNote
import com.dev_musashi.note.domain.usecase.GetNote
import com.dev_musashi.note.util.SnackBarManager
import com.dev_musashi.note.R.string as AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val addNote: AddNote,
    private val getNote: GetNote
) : ViewModel() {

    var state = mutableStateOf(NoteState())
        private set

    private var noteId: Int? = null
    private val title get() = state.value.title
    private val content get() = state.value.content
    private val color get() = state.value.color

    fun init(id: Int){
        if(id != -1){
            noteId = id
            viewModelScope.launch(Dispatchers.IO) {
                val note = getNote(id)
                if(note != null) {
                    state.value = state.value.copy(
                        title = note.title,
                        content = note.content,
                        color = note.color,
                        titleHint = false,
                        contentHint = false
                    )
                }
            }
        }


    }

    fun onTitleChanged(newTitle: String) {
        state.value = state.value.copy(title = newTitle)
    }

    fun onTitleHintChanged(titleFocus: FocusState) {
        state.value = state.value.copy(titleHint = !titleFocus.isFocused && title.isBlank())
    }

    fun onContentHintChanged(contentFocus: FocusState) {
        state.value = state.value.copy(contentHint = !contentFocus.isFocused && content.isBlank())
    }

    fun onContentChanged(newContent: String) {
        state.value = state.value.copy(content = newContent)
    }

    fun onColorChanged(newColor: Int) {
        state.value = state.value.copy(color = newColor)
    }

    fun onColorSectionVisible(colorSection: Boolean){
        state.value = state.value.copy(colorSection = !colorSection)
    }

    fun onSaveClick(
        popUpScreen: ()->Unit
    ){
        viewModelScope.launch {
            if(title.isNotBlank() && content.isNotBlank()) {
                addNote(
                    note = Note(
                        id = noteId,
                        title = title,
                        content = content,
                        color = color,
                        timestamp = System.currentTimeMillis()
                    )
                )
                popUpScreen()
            }
            else {
                if(title == ""){
                    SnackBarManager.showMessage(AppText.titleHint)
                }
                if(content == ""){
                    SnackBarManager.showMessage(AppText.contentHint)
                }

            }
        }

    }

    fun onBack(popUpScreen: () -> Unit) {
        popUpScreen()
    }
}