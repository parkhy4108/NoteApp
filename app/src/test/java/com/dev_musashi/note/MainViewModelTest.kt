package com.dev_young.note

import com.dev_young.note.domain.model.Note
import com.dev_young.note.domain.usecase.AddNote
import com.dev_young.note.domain.usecase.DeleteNote
import com.dev_young.note.domain.usecase.GetNotes
import com.dev_young.note.presentation.notesList.MainViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private val notesTest = mutableListOf<Note>()
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getNotes: GetNotes
    private lateinit var addNote: AddNote
    private lateinit var deleteNote: DeleteNote

    @Before
    fun setUp() {

        (0..10).forEach { i ->
            notesTest.add(Note(i, "$i", "$i", i.toLong(), i))
        }

        repository = FakeRepository()

        getNotes = GetNotes(repository)
        addNote = AddNote(repository)
        deleteNote = DeleteNote(repository)

        viewModel = MainViewModel(getNotes, addNote, deleteNote)

        runBlocking {
            notesTest.forEach {
                repository.insertNote(it)
            }
        }

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAllNotes() is called, then the notes are sorted and updated in the state`() =
        runTest {
            //when
            val state = viewModel.state.value
            viewModel.getAllNotes()
            delay(2000)

            //then
            assertEquals(notesTest, viewModel.state.value.notes.sortedBy { it.id })
        }

    @Test
    fun `when deleteButtonClick() is called, then the note is deleted and recently note is updated`() =
        runTest {
            //given
            val note = Note(0, "0", "0", 0L, 0)
            notesTest.remove(note)

            //when
            viewModel.deleteButtonClick(note)

            //then
            viewModel.getAllNotes()
            delay(1000)
            val result = viewModel.state.value.notes.sortedBy { it.id }
            assertEquals(note, viewModel.recentlyDeletedNote)
            assertEquals(notesTest, result)

        }

    @Test
    fun `when undoClick is called and recently DeleteNote is not Null, then the note is added back`() =
        runTest {

            //given
            notesTest.add(Note(0, "0", "0", 0L, 0))
            viewModel.recentlyDeletedNote = Note(0, "0", "0", 0L, 0)

            //when
            viewModel.undoClick()

            //then
            viewModel.getAllNotes()
            delay(1000)
            val result = viewModel.state.value.notes.sortedBy { it.id }
            assertEquals(notesTest.sortedBy { it.id }, result)

        }

    @Test
    fun `when addButtonClick() is called, then the new note screen is opened with id`() {
        //given
        var calledRoute: String? = null
        val openScreen : (String) -> Unit = { route -> calledRoute = route}

        //when
        viewModel.addButtonClick(openScreen)

        //then
        assertEquals(Screen.Note.route, calledRoute)
    }

    @Test
    fun `when noteClick is called, then the note screen is opened with Note id`(){
        //given
        val id = 123
        var calledRoute: String? = null
        val openScreen: (String) -> Unit = { route -> calledRoute = route}

        //when
        viewModel.noteClick(id, openScreen)

        //then
        assertEquals(Screen.Note.passId(id), calledRoute)
        println(calledRoute)
    }


}

