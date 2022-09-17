package com.dev_musashi.note.presentation.add

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_musashi.note.ui.theme.*
import com.dev_musashi.note.util.addFocusCleaner
import com.dev_musashi.note.R.drawable as AppImg
import com.dev_musashi.note.R.string as AppText

@Composable
fun NoteScreen(
    id: Int,
    popUpScreen: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val noteColorsList0 = listOf(Color0, Color1, Color2, Color3, Color4)
    val noteColorsList1 = listOf(Color5, Color6, Color7, Color8, Color9)
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.init(id)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onSaveClick(popUpScreen) }) {
                Icon(painter = painterResource(id = AppImg.ic_save), contentDescription = null)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color(state.color))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .background(Color(state.color))
                    .addFocusCleaner(focusManager)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { viewModel.onBack(popUpScreen) }) {
                            Icon(
                                painter = painterResource(id = AppImg.ic_back),
                                contentDescription = null,
                                tint = if (state.color == Color(0xFF1B1B1A).toArgb()) Color.White else Color.Black
                            )
                        }
                        TextButton(onClick = { viewModel.onColorSectionVisible(state.colorSection) }) {
                            Text(
                                text = stringResource(id = AppText.color),
                                color = if (state.color == Color(0xFF1B1B1A).toArgb()) Color.White else Color.Black
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.colorSection,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                noteColorsList0.forEach { color ->
                                    val colorInt = color.toArgb()
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(color = color)
                                            .border(
                                                width = 2.dp,
                                                color =
                                                if (state.color == colorInt)
                                                    Color.Black
                                                else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                viewModel.onColorChanged(colorInt)
                                            }
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                noteColorsList1.forEach { color ->
                                    val colorInt = color.toArgb()
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(color = color)
                                            .border(
                                                width = 2.dp,
                                                color = if (state.color == colorInt) Color.Black else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                viewModel.onColorChanged(color.toArgb())
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
                Divider(color = Color.LightGray)
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    focusRequester = focusRequester,
                    focusChange = { viewModel.onTitleHintChanged(it) },
                    text = state.title,
                    onValueChange = { viewModel.onTitleChanged(it) },
                    color = state.color,
                    hint = state.titleHint,
                    hintText = AppText.title,
                    singleLine = true
                )
                Divider(color = Color.LightGray)
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    focusRequester = focusRequester,
                    focusChange = { viewModel.onContentHintChanged(it) },
                    text = state.content,
                    onValueChange = { viewModel.onContentChanged(it) },
                    color = state.color,
                    hint = state.contentHint,
                    hintText = AppText.content,
                    singleLine = false
                )
            }
        }

    }

}

@Composable
fun TextField(
    modifier: Modifier,
    focusRequester: FocusRequester,
    focusChange: (FocusState) -> Unit,
    text: String,
    onValueChange: (String) -> Unit,
    color: Int,
    hint: Boolean,
    hintText: Int,
    singleLine: Boolean
) {
    val textColor = if (color == Color(0xFF1B1B1A).toArgb()) Color.White else Color.Gray
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusChange(it) },
            value = text,
            onValueChange = { onValueChange(it) },
            singleLine = singleLine,
            textStyle = LocalTextStyle.current.copy(color = textColor),
            cursorBrush = SolidColor(textColor)
        )
        if (hint)
            Text(
                text = stringResource(id = hintText),
                color = textColor
            )
    }
}