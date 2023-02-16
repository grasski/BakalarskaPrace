package zoo.animals.feature_camera.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.key
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import zoo.animals.feature_camera.ImageClassifier
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import java.util.concurrent.ExecutorService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CameraViewModel: ViewModel() {
    val state by mutableStateOf(CameraState())

    fun analyze(analyzer: ImageAnalysis, cameraExecutor: ExecutorService, context: Context){
        if (!state.classificationRunning.value){
            state.classifiedAnimal.value = null
            analyzer.clearAnalyzer()
        } else{
            analyzer.setAnalyzer(cameraExecutor) { imageProxy ->
//                val classified = ImageClassifier(context).classify(imageProxy)
//
//                val animalName = classified[0] as String
//                state.classifiedAnimal.value = (AnimalData.allAnimalsInstance[0] + AnimalData.allAnimalsInstance[1] + AnimalData.allAnimalsInstance[2])[animalName.trim()]

                val detected = ImageClassifier(context).detect(imageProxy)
                val animalName = detected[0] as String

                state.classifiedAnimal.value = (AnimalData.allAnimalsInstance[0] + AnimalData.allAnimalsInstance[1] + AnimalData.allAnimalsInstance[2])[animalName.trim()]

                state.detectionBox.value = detected[1] as RectF
                state.testingText.value = detected[2].toString() + " " + detected[1]
            }
        }
    }

    suspend fun setupCamera(context: Context, lifecycleOwner: LifecycleOwner, preview: Preview){
        state.cameraProvider.value = context.getCameraProvider()
        state.cameraProvider.value?.unbindAll()

        state.camera.value = state.cameraProvider.value?.bindToLifecycle(
            lifecycleOwner,
            state.cameraSelector,
            preview,
            state.imageCapture,
            state.imageAnalysis
        )
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
