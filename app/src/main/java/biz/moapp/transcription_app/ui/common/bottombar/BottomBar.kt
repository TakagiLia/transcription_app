package biz.moapp.transcription_app.ui.common.bottombar

import android.util.Log
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import biz.moapp.transcription_app.navigation.Nav
import biz.moapp.transcription_app.ui.theme.BottomBarIconColor
import biz.moapp.transcription_app.ui.theme.Gray333333
import biz.moapp.transcription_app.ui.theme.Viridian

@Composable
fun BottomBar(navController : NavHostController){

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(containerColor = Viridian, contentColor = Gray333333) {
        NAVIGATION_ITEMS.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = BottomBarIconColor,
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index

                    when(selectedItemIndex){
                        0 -> navController.navigate(Nav.MainScreen.name)
                        1 -> navController.navigate(Nav.SummaryScreen.name)
                        2 -> Log.d("--BottomBar","right")
                    }
                },
                label = {
                    Text(text = item.title, color = Gray333333)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if (item.hasNews) {
                                Badge()
                            }
                        }
                    )
                    {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                },
            )
        }
    }
}