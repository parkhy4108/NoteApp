package com.dev_musashi.note.presentation.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_musashi.note.domain.model.Note
import kotlinx.coroutines.launch
import com.dev_musashi.note.R.drawable as AppImg
import com.dev_musashi.note.R.string as AppText

@Composable
fun MainScreen(
    openScreen: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
){
    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.addButtonClick(openScreen)},
                backgroundColor = Color(0xFF4DD0E1)
            ) {
                Icon(painter = painterResource(id = AppImg.ic_add), contentDescription = null)
            }
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = AppText.MyNote),
                    style = MaterialTheme.typography.h5
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.notes) { note ->
                    NoteBox(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                border = BorderStroke(2.dp, color = Color.LightGray),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(color = Color(note.color))
                            .clickable { viewModel.noteClick(note.id!!, openScreen) }
                        ,
                        onDeleteClick = {
                            viewModel.deleteButtonClick(note = note)
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "삭제되었습니다",
                                    actionLabel = "취소"
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    viewModel.undoClick()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun NoteBox(
    note: Note,
    modifier : Modifier,
    onDeleteClick: ()->Unit
){
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                maxLines = 5,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = { onDeleteClick() },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(painter = painterResource(id = AppImg.ic_delete), contentDescription = null)
        }
    }
}