package biz.moapp.transcription_app.ui.common.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import biz.moapp.transcription_app.navigation.Nav

/**BottomBarのアイテムのデータクラス**/
data class BottomNavigationItem(
    val screenName: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)
/**BottomBarのアイテムのリスト**/
val NAVIGATION_ITEMS = listOf(
    BottomNavigationItem(
        screenName = Nav.MainScreen.name,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
    ),
    BottomNavigationItem(
        screenName = Nav.SummaryScreen.name,
        selectedIcon = Icons.Filled.Summarize,
        unselectedIcon = Icons.Filled.Summarize,
        hasNews = false,
    ),
)