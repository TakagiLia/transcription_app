package biz.moapp.transcription_app.ui.summary

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.ui.compose.EditField
import biz.moapp.transcription_app.ui.compose.OperationButton
import biz.moapp.transcription_app.ui.main.MainScreenViewModel
import biz.moapp.transcription_app.ui.state.MainUiState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SummaryScreen(modifier : Modifier, mainScreenViewModel: MainScreenViewModel){

    var isEditable by remember { mutableStateOf(false) }
    var initialized by remember { mutableStateOf(false) }

    /**初期表示のための処理**/
    LaunchedEffect(Unit) {
        if (!initialized) {
            mainScreenViewModel.summary(mainScreenViewModel.audioText.value)
            initialized = true
        }
    }

    val systemColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val maxModifierButton : Modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)

    /**UI**/
    Column(modifier = modifier
        .fillMaxHeight(0.75f)
        .fillMaxWidth(1f)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /**要約時の結果表示**/
        when (mainScreenViewModel.uiState.sendResultState) {
            is MainUiState.SendResultState.NotYet -> Unit
            is MainUiState.SendResultState.Loading -> {
                CircularProgressIndicator()
            }
            is MainUiState.SendResultState.Success -> {
                (mainScreenViewModel.uiState.sendResultState as MainUiState.SendResultState.Success).results.map { value ->
                    Log.d("--result response：　", value)
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        border = BorderStroke(1.dp, systemColor),
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Text(
                            text = stringResource(R.string.summary_title),
                            color = systemColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )
                        EditField(mainScreenViewModel,isEditable)
                    }
                }
            }
            is MainUiState.SendResultState.Error -> {}
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            /**編集ボタン**/
            OperationButton(
                modifier = maxModifierButton,
                buttonName = if(!isEditable) stringResource(R.string.summary_edit) else stringResource(R.string.summary_change),
                clickAction = {
                    isEditable = !isEditable
                }
            )
        /**要約した内容の保存**/
        OperationButton(
            modifier = maxModifierButton,
            buttonName = stringResource(R.string.summary_save),
            clickAction = { mainScreenViewModel.summarySave(mainScreenViewModel.summaryText.value) }
        )
    }
}