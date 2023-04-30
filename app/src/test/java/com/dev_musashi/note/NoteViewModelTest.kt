package com.dev_young.note

import com.dev_young.note.domain.model.Note
import com.dev_young.note.domain.usecase.AddNote
import com.dev_young.note.domain.usecase.GetNote
import com.dev_young.note.presentation.note.NoteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class NoteViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var fakeRepository: FakeRepository

    private lateinit var getNote: GetNote
    private lateinit var addNote: AddNote

    private val notesTest = mutableListOf<Note>()

    private fun noteInit() {
        (0..2).forEachIndexed { index, c ->
            notesTest.add(
                Note(
                    id = index,
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
    }

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        getNote = GetNote(fakeRepository)
        addNote = AddNote(fakeRepository)
        noteViewModel = NoteViewModel(addNote, getNote)
        noteInit()
        runBlocking {
            notesTest.forEach { fakeRepository.insertNote(it) }
        }
    }

    @Test
    fun `when init() is called, then the note is found by id`() = runTest {
        //given
        val note = notesTest.find { it.id == 0 }!!.title

        //when
        noteViewModel.init(0)
        delay(1000)

        //then
        assertEquals(note, noteViewModel.state.value.title)
    }

    @Test
    fun `when addNote() is called, then the current note is added`() = runTest {
        //given
        noteViewModel.noteId = 3
        noteViewModel.state.value = noteViewModel.state.value.copy(title = "3", content = "3", color = 3)

        //when
        noteViewModel.addNoteClicked()
        notesTest.add(
            Note(
                id = 3,
                title = "3",
                content = "3",
                color = 3,
                timestamp = noteViewModel.currentTime!!
            )
        )
        delay(1000)

        //then
        val result = fakeRepository.getNotes().first().sortedBy { it.id }
        assertEquals(notesTest.sortedBy { it.id },result )

    }

    @Test
    fun `when onTitleChanged() is called, then note title is changed in the State`() {
        //given
        val newTitle = "newTitle"

        //when
        noteViewModel.onTitleChanged(newTitle)

        //then
        assertEquals(newTitle, noteViewModel.state.value.title)
    }

    @Test
    fun `when onTitleHintChanged() is called with titleFocus is true and title is true, then title hint state is changed in the State`() {
        //when
        noteViewModel.onTitleHintChanged(titleFocus = true, title = true)

        //then
        assertEquals(true, noteViewModel.state.value.titleHint)

        //when
        noteViewModel.onTitleHintChanged(titleFocus = false, title = false)

        //then
        assertEquals(false, false)

    }

    @Test
    fun `when onContentChanged() is called, then note content is changed in the state`() {

        //when
        noteViewModel.onContentChanged("newContent")

        //then
        assertEquals("newContent", noteViewModel.state.value.content)


        //when
        noteViewModel.onContentChanged(" ")

        //then
        assertEquals(" ", noteViewModel.state.value.content)
    }


}
