package zoo.animals.feature_camera.model

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import zoo.animals.feature_camera.CameraExtensions

data class CaptureState(
    var imageUri: MutableState<Uri?> = mutableStateOf(null),

    val cameraExtensions: CameraExtensions = CameraExtensions(),
)
