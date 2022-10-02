package zoo.animals.feature_camera

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.navigation.NavController
import zoo.animals.shared.BottomContentSwitch


class CameraScreen {

    @SuppressLint("NotConstructor")
    @Composable
    fun CameraScreen(navController: NavController){
        BottomContentSwitch(
            mainPreview = { CameraPreviewScreen(navController = navController) },
            secondPreview = { PhotoRecognitionScreen(navController = navController) }
        )
    }
}