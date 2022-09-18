package zoo.animals.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.navigation.NavController
import zoo.animals.BottomCameraSwitch
import zoo.animals.camera.CameraPreviewScreen
import zoo.animals.camera.PhotoRecognitionScreen

class CameraScreen {

    @SuppressLint("NotConstructor")
    @Composable
    fun CameraScreen(navController: NavController){
        BottomCameraSwitch(
            cameraPreview = { CameraPreviewScreen(navController = navController) },
            galleryPreview = { PhotoRecognitionScreen(navController = navController) }
        )
    }
    
}