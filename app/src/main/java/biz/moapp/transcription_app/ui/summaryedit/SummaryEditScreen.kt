package biz.moapp.transcription_app.ui.summaryedit

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import biz.moapp.transcription_app.ui.compose.OperationButton
import biz.moapp.transcription_app.ui.main.MainScreenViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SummaryEditScreen(mainScreenViewModel: MainScreenViewModel, onNavigateToMain: () -> Unit){

    var text by remember { mutableStateOf(mainScreenViewModel.AudioText.value) }
    val systemColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(1.5.dp, Color.Black)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it
                        mainScreenViewModel.setAudioText(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(color = systemColor)
                )
            }
            OperationButton(Modifier.fillMaxWidth(),"save") { onNavigateToMain() }
        }

    }
}
