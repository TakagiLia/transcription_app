package biz.moapp.transcription_app.ui.common.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**BottomBarのアイテムのデータクラス**/
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

/**BottomBarのアイテムのリスト**/
val NAVIGATION_ITEMS = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
    ),
    BottomNavigationItem(
        title = "Summary",
        selectedIcon = Icons.Filled.Summarize,
        unselectedIcon = Icons.Filled.Summarize,
        hasNews = false,
    ),
)