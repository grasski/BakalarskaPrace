package zoo.animals.feature_camera.view

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.navigation.NavController
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