package com.dev_musashi.note

import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.usecase.AddNote
import com.dev_musashi.note.domain.usecase.DeleteNote
import com.dev_musashi.note.domain.usecase.GetNotes
import com.dev_musashi.note.presentation.main.MainViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 테스트 대상 클래스
    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepository: FakeRepository

    private lateinit var getNotes: GetNotes
    private lateinit var addNote: AddNote
    private lateinit var deleteNote: DeleteNote

    private val notesTest = mutableListOf<Note>()

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        getNotes = GetNotes(repository = fakeRepository)
        deleteNote = DeleteNote(repository = fakeRepository)
        addNote = AddNote(repository = fakeRepository)

        viewModel = MainViewModel(getNotes, addNote, deleteNote)


        runBlocking {
            notesTest.forEach { fakeRepository.insertNote(it) }
        }
    }

    @Test
    fun `viewModel이 시작되면, 모든 노트들을 불러옴`() = runTest {
        viewModel.getAllNotes()
        assertEquals(notesTest, viewModel.state.value.notes.sortedBy { it.id })
    }

    @Test
    fun `삭제버튼을 누르면 노트가 삭제됨`() = runTest {
        notesTest.remove(Note(0,"a","a",0L,0))
        viewModel.deleteButtonClick(Note(0,"a","a",0L,0))
        viewModel.getAllNotes()
        assertEquals(notesTest,viewModel.state.value.notes.sortedBy { it.id })
    }

    @Test
    fun `undo버튼을 누르면 삭제한 노트가 다시 복구됨`() = runTest {
        viewModel.undoClick()
        viewModel.getAllNotes()
        assertEquals(notesTest, viewModel.state.value.notes.sortedBy { it.id })
    }


}