package biz.moapp.transcription_app.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HelpTextInIcon(imageVector : ImageVector, helpText : String){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentHeight()) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = Modifier
                .size(24.dp)
        )
        Text(text = helpText, textAlign = TextAlign.Center,)
    }
}