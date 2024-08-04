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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.navigation.Nav

@Composable
fun BottomBar(navController : NavHostController){

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route?.substringBefore("/")

    NavigationBar() {
        NAVIGATION_ITEMS.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = item.screenName == selectedRoute,
                onClick = {
                    selectedItemIndex = index
                    Log.d("--Navigation","onClick selectedItemIndex${selectedItemIndex}")
                    /**アイコンの遷移先**/
                    when(selectedItemIndex){
                        0 -> navController.navigate(Nav.MainScreen.name)
                        1 -> navController.navigate("${Nav.SummaryScreen.name}/bottomBar")
                        2 -> Log.d("--BottomBar","right")
                    }
                },
                label = {
                    Log.d("--Navigation","label index${index}")
                    /**アイコンのラベル取得**/
                    val title = when(index){
                        0 -> stringResource(R.string.navigationbar_home)
                        1 -> stringResource(R.string.navigationbar_summary)
                        else -> {""}
                    }
                    Log.d("--Navigation","title${title}")
                    Text(text = title)
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
                            contentDescription = ""
                        )
                    }
                },
            )
        }
    }
}