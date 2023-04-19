package com.dev_musashi.note

import com.dev_musashi.note.domain.model.Note
import com.dev_musashi.note.domain.usecase.AddNote
import com.dev_musashi.note.domain.usecase.GetNote
import com.dev_musashi.note.presentation.add.NoteViewModel
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
class NoteViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    //테스트 대상 클래스
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var fakeRepository: FakeRepository

    private lateinit var getNote: GetNote
    private lateinit var addNote: AddNote

    private val notesTest = mutableListOf<Note>()

    fun noteInit(){
        ('a'..'z').forEachIndexed { index, c ->
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
        noteViewModel= NoteViewModel(addNote, getNote)
        noteInit()
        runBlocking {
            notesTest.forEach { fakeRepository.insertNote(it) }
        }
    }

    @Test
    fun `저장 버튼을 누르면, 현재 노트를 저장함`() = runTest {
        noteViewModel.noteId = 111
        noteViewModel.state.value = noteViewModel.state.value.copy(title = "new", content = "new", color = 1)
        noteViewModel.addNote().also {
            notesTest.add(Note(
                id = 111,
                title = "new",
                content = "new",
                color = 1,
                timestamp = System.currentTimeMillis()
            ))
        }
        assertEquals(notesTest, fakeRepository.getNotes().first())

    }

    @Test
    fun `기존 노트의 id, id에 맞는 노트를 찾아옴`() = runTest {
        noteViewModel.init(0)
        val note = notesTest.find { it.id == 0 }!!.title
        assertEquals(note, noteViewModel.state.value.title)
    }
}