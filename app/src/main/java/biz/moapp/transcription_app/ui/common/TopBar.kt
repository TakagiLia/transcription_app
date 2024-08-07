package biz.moapp.transcription_app.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.navigation.Nav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigateBack : NavHostController){

    val backStackEntry by navigateBack.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route?.substringBefore("/")

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if(selectedRoute != Nav.MainScreen.name){
                IconButton(onClick = {navigateBack.navigate(Nav.MainScreen.name)}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
//        actions = {
//            Icon(
//                imageVector = Icons.Filled.AccountCircle,
//                contentDescription = "Localized description"
//            )
//        },
    )

}