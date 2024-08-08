package biz.moapp.transcription_app.ui.summary

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavHostController
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.ui.common.TopBar
import biz.moapp.transcription_app.ui.common.bottombar.BottomBar
import biz.moapp.transcription_app.ui.compose.EditField
import biz.moapp.transcription_app.ui.main.MainScreenViewModel
import biz.moapp.transcription_app.ui.state.MainUiState

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SummaryScreen(mainScreenViewModel: MainScreenViewModel, action: String,navController:NavHostController ) {


    var isEditable by remember { mutableStateOf(false) }
    var initialized by remember { mutableStateOf(false) }

    /**初期表示のための処理**/
    LaunchedEffect(Unit) {
        if (!initialized && action == "summarize") {
            mainScreenViewModel.summary(mainScreenViewModel.audioText.value)
            initialized = true
        }
    }

    val systemColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val maxModifierButton: Modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)

    /**画面サイズの取得**/
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopBar(navController) }, bottomBar = { BottomBar(navController) }) { innerPadding ->
        BoxWithConstraints {
            val width = maxWidth
            val height = maxHeight

            /**UI**/
            Column(
                modifier = Modifier.padding(innerPadding)
        //        .fillMaxHeight(0.75f)
                    .fillMaxWidth(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /**要約時の結果表示**/
                when (mainScreenViewModel.uiState.sendResultState) {
                    is MainUiState.SendResultState.NotYet -> Column(modifier = Modifier.padding(innerPadding).padding(top = (width * 0.5f),)) {
                        Text(
                            stringResource(R.string.summary_no_content)
                        )
                    }

                    is MainUiState.SendResultState.Loading -> {
                        Column(modifier = Modifier.padding(innerPadding).padding(top = (width * 0.4f),)) {
                            CircularProgressIndicator()
                        }
                    }

                    is MainUiState.SendResultState.Success -> {
                        (mainScreenViewModel.uiState.sendResultState as MainUiState.SendResultState.Success).results.map { value ->
                            Log.d("--result response：　", value)
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedCard(
                                border = BorderStroke(1.dp, systemColor),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.summary_title),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(4.dp)
                                )
                                EditField(mainScreenViewModel, isEditable)
                            }
                        }
                    }

                    is MainUiState.SendResultState.Error -> {}
                }
            }
        }
//    Column(
//        modifier = modifier
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Bottom,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//            /**編集ボタン**/
//            OperationButton(
//                modifier = maxModifierButton,
//                buttonName = if(!isEditable) stringResource(R.string.summary_edit) else stringResource(R.string.summary_change),
//                clickAction = {
//                    isEditable = !isEditable
//                }
//            )
//        /**要約した内容の保存**/
//        OperationButton(
//            modifier = maxModifierButton,
//            buttonName = stringResource(R.string.summary_save),
//            clickAction = { mainScreenViewModel.summarySave(mainScreenViewModel.summaryText.value) }
//        )
//    }
    }
}