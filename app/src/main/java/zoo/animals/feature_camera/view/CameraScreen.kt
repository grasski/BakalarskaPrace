package zoo.animals.feature_camera.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.shared.BottomContentSwitch


class CameraScreen {

    @SuppressLint("NotConstructor")
    @Composable
    fun CameraScreen(navController: NavController){
        BottomContentSwitch(
            UiTexts.StringResource(R.string.liveCamera).asString(),
            UiTexts.StringResource(R.string.photoSelect).asString(),
            null,
            mainPreview = { CameraPreviewScreen(navController = navController) },
            secondPreview = { PhotoRecognitionScreen(navController = navController) },
            null
        )
    }
}