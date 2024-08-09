package biz.moapp.transcription_app.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import biz.moapp.transcription_app.R
import biz.moapp.transcription_app.ui.theme.errorLight

@Composable
fun RecordingButton(isEnable: Boolean,
                    buttonName : String,
                    icon : ImageVector,
                    onToggle: (Boolean) -> Unit) {
    val backGroundColor = when (buttonName) {
        /**完了ボタンのみは赤色と灰色**/
        stringResource(R.string.recording_complete) -> if (isEnable) errorLight else MaterialTheme.colorScheme.surfaceVariant
        /**完了ボタン以外はプライマリーカラー**/
        else -> MaterialTheme.colorScheme.primary
    }
    IconButton(
        onClick = {
            onToggle(!isEnable)
        },
        modifier = Modifier
            .size(64.dp)
            .background(backGroundColor, CircleShape),
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            contentDescription = "",
            tint = Color.White
        )
    }
}