package biz.moapp.transcription_app.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import biz.moapp.transcription_app.ui.main.MainScreenViewModel

@Composable
fun EditField(mainScreenViewModel: MainScreenViewModel,enabled :Boolean){

    var text by remember { mutableStateOf(mainScreenViewModel.summaryText.value) }
    val systemColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    BasicTextField(
        value = text,
        enabled = enabled,
        onValueChange = {
            text = it
            mainScreenViewModel.setSummaryText(it) },
        modifier = Modifier.fillMaxSize().padding(8.dp),
        textStyle = TextStyle(color = systemColor)
    )
}