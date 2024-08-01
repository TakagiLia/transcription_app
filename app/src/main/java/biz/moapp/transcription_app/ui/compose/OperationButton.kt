package biz.moapp.transcription_app.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OperationButton(modifier: Modifier, enabled: Boolean = true, buttonName : String, clickAction:() -> Unit){
    Button(modifier = modifier
        .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        border = BorderStroke(1.dp, Color.Black),
        onClick = {clickAction()}){
        Text(modifier = Modifier.padding(6.dp), text = buttonName)
    }
}