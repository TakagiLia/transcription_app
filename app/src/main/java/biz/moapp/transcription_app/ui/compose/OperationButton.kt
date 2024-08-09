package biz.moapp.transcription_app.ui.compose

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun OperationButton(modifier: Modifier, enabled: Boolean = true, buttonName : String, icon : ImageVector? = null,
                    clickAction:() -> Unit){
    var shadowButton = remember { mutableIntStateOf(6) }
    Button(modifier = modifier
        .padding(16.dp),
        shape = RoundedCornerShape(40.dp),
        enabled = enabled,
        elevation =  ButtonDefaults.buttonElevation(defaultElevation = shadowButton.intValue.dp,),
        onClick = {
            shadowButton.intValue = 0
            clickAction()
            Handler(Looper.getMainLooper()).postDelayed({
                shadowButton.intValue = 6
            }, 200)
        }){
        icon?.let{
            Icon(imageVector = it,contentDescription = "")
        }
        Text(modifier = Modifier.padding(6.dp), text = buttonName)
    }
}