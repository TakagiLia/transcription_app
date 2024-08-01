package biz.moapp.transcription_app.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.navigation.Nav
import biz.moapp.transcription_app.ui.theme.Gray333333
import biz.moapp.transcription_app.ui.theme.Viridian

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigateBack : NavHostController){

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.topbar_title),
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Viridian,
            navigationIconContentColor = Gray333333,
            titleContentColor = Gray333333,
            actionIconContentColor = Gray333333
        ),
        navigationIcon = {
            IconButton(onClick = {navigateBack.navigate(Nav.MainScreen.name)}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Localized description"
            )
        },
    )

}