package zoo.animals.feature_camera.model

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import zoo.animals.feature_camera.ImageClassifier
import zoo.animals.feature_category.data.Animal
import java.util.concurrent.ExecutorService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CameraViewModel: ViewModel() {
    val state by mutableStateOf(CameraState())

    fun analyze(analyzer: ImageAnalysis, cameraExecutor: ExecutorService, context: Context){
        analyzer.setAnalyzer(cameraExecutor) { imageProxy ->
            val detected = ImageClassifier(context).classify(imageProxy)

            val animalName = detected[0] as String
            val animal: Animal? = state.classifiedAnimal?.get(animalName)
            animal?.let { an ->
                state.classifiedAnimal?.put(animalName, an)
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
