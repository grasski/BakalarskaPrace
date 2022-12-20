package zoo.animals.feature_camera.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CaptureViewModel : ViewModel() {
    val state by mutableStateOf(CaptureState())

    fun takePhoto(cameraViewModel: CameraViewModel, context: Context){
        state.cameraExtensions.takePhoto(
            cameraViewModel.state.imageCapture,
            cameraViewModel.state.cameraExecutor,
            context,
            onImageCaptured = { state.imageUri.value = it },
            onError = { state.imageUri.value = null }
        )
    }
}