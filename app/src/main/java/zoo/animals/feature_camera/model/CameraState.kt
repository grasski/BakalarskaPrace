package zoo.animals.feature_camera.model

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class CameraState(
    var classifiedAnimal: SnapshotStateMap<String, Animal>? = null,
    var classificationRunning: MutableState<Boolean> = mutableStateOf(true),
    val animals: MutableList<MutableMap<String, Animal>> = AnimalData.allAnimalsInstance,

    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
    val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    val cameraProvider: MutableState<ProcessCameraProvider?> = mutableStateOf(null),
    val camera: MutableState<Camera?> = mutableStateOf(null),

    var currentZoom: MutableState<Float> = mutableStateOf(0f),

    val imageCapture: ImageCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .setJpegQuality(100)
        .build(),

    var imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build(),

    val cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build(),

)