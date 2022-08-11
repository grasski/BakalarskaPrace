package zoo.animals.camera


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.Surface.ROTATION_90
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.common.FileUtil
import zoo.animals.R
import zoo.animals.Routes
import zoo.animals.UiTexts
import zoo.animals.data.AnimalData
import zoo.animals.screens.CameraSheetInfo
import zoo.animals.screens.ShowAnimalInfo
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalPermissionsApi
@SuppressLint("UnsafeOptInUsageError", "RestrictedApi", "CoroutineCreationDuringComposition",
    "ClickableViewAccessibility"
)
@Composable
fun SimpleCameraPreview(navController: NavController) {
    val permissions = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ))

    var imBitmap by remember {
        mutableStateOf(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
    }
    var recognitionRunning by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf("") }
    var currentZoomRatio by remember { mutableStateOf(0f) }
    var delta by remember { mutableStateOf(0f) }

    val context = LocalContext.current
    val animalLabels = remember { FileUtil.loadLabels(context, "labels.txt") }
    val animalsTranslate = remember { UiTexts.ArrayResource(R.array.labels, 0).asArray(context) }

    val mammals = rememberSaveable { AnimalData.mammals(context) }
    val birds = rememberSaveable { AnimalData.birds(context) }
    val reptiles = rememberSaveable { AnimalData.reptiles(context) }
    val catalogAnimals = mammals + birds + reptiles


    val animals: Map<String, String> = rememberSaveable { animalLabels.zip(animalsTranslate).toMap() }
    var animal by remember { mutableStateOf(catalogAnimals[animals[result]]) }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenSheetHalfHeight = rememberSaveable { (screenHeight/2) + (screenHeight/16) }
    var sheetDisplayingHeight by remember { mutableStateOf(screenSheetHalfHeight) }
    var fractionPercentage by remember { mutableStateOf((sheetState.progress.fraction * 100).dp) }

    val barDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var draggerAlpha by remember { mutableStateOf(1f) }

    ModalBottomSheetLayout(
        sheetElevation = 0.dp,
        sheetState = sheetState,
        sheetContent = {
            BackHandler {
                scope.launch {
                    sheetState.hide()
                }
            }

            fractionPercentage = (sheetState.progress.fraction * 100).dp
            Box(modifier = Modifier.graphicsLayer {
                if (sheetState.targetValue == ModalBottomSheetValue.Hidden) {
                    alpha = 1f - sheetState.progress.fraction
                }
            })
            {
                Column {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                    ) {
                        Box(modifier = Modifier.absoluteOffset(0.dp, (-sheetDisplayingHeight).dp)){
                            CameraSheetInfo(animal)
                        }
                        Box(modifier = Modifier.absoluteOffset(0.dp, (screenSheetHalfHeight-sheetDisplayingHeight).dp)) {
                            ShowAnimalInfo(navController, animal, false)
                        }

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp)
                                .graphicsLayer { alpha = draggerAlpha }
                                .height(20.dp)
                                .background(MaterialTheme.colors.background)
                                .align(Alignment.TopCenter)
                        ){
                            Spacer(modifier =
                            Modifier
                                .width(90.dp)
                                .height(3.5.dp)
                                .background(Color.White)
                                .align(Alignment.Center)
                                .background(MaterialTheme.colors.contentColorFor(Color.White))
                            )
                        }
                    }
                }
            }
        }
    ) {
        Box{
            if (sheetState.progress.to == ModalBottomSheetValue.Expanded &&
                sheetState.progress.from == ModalBottomSheetValue.HalfExpanded
            ) {
                sheetDisplayingHeight =
                    ((screenSheetHalfHeight * fractionPercentage.value) / 100).toInt() % screenSheetHalfHeight
                draggerAlpha = 1f
            } else if (sheetState.progress.to == ModalBottomSheetValue.HalfExpanded &&
                sheetState.progress.from == ModalBottomSheetValue.Expanded
            ) {
                sheetDisplayingHeight =
                    screenSheetHalfHeight-((screenSheetHalfHeight * fractionPercentage.value) / 100).toInt()
                draggerAlpha = 1f
            } else if (
                (sheetState.progress.to == ModalBottomSheetValue.Expanded &&
                 sheetState.progress.from == ModalBottomSheetValue.Expanded)
            ) {
                sheetDisplayingHeight = screenSheetHalfHeight
                draggerAlpha = 0f
            } else if (sheetState.currentValue == ModalBottomSheetValue.HalfExpanded ||
                sheetState.currentValue == ModalBottomSheetValue.Hidden ||
                (sheetState.progress.to == ModalBottomSheetValue.Hidden &&
                 sheetState.progress.from == ModalBottomSheetValue.HalfExpanded &&
                 sheetState.currentValue == ModalBottomSheetValue.Expanded)
            ) {
                sheetDisplayingHeight = 0
                draggerAlpha = 1f
            }

            Column {
                // Secure checking for camera permission if the app has been minimized and changed permissions manually in settings.
                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                permissions.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                if (permissions.permissions[0].status.isGranted) {
                    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
                    val lensFacing = CameraSelector.LENS_FACING_BACK
                    val lifecycleCurrentOwner = LocalLifecycleOwner.current

                    val preview = Preview.Builder().build()
                    val previewView = remember { PreviewView(context) }


                    val analysis = remember {
                        ImageAnalysis.Builder()
                            .setTargetRotation(ROTATION_90)
                            .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_YUV_420_888)
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                    }
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()

                    var tempResult by remember {
                        mutableStateOf("")
                    }
                    if (recognitionRunning){
                        if (barDrawerState.currentValue == DrawerValue.Closed && (sheetState.progress.fraction == 1f && !sheetState.isVisible)) {
                            analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                val classified = ImageClassifier(context).classify(imageProxy)
                                tempResult = classified[0] as String
                                imBitmap = classified[1] as Bitmap?
                            }
                        } else {
                            Image(
                                bitmap = imBitmap.asImageBitmap(),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.TopCenter
                            )
                            analysis.clearAnalyzer()
                        }
                    } else{
                        analysis.clearAnalyzer()
                    }


                    if (tempResult != "") {
                        result = tempResult
                        tempResult = ""
                        if (catalogAnimals.containsKey(animals[result])){
                            animal = catalogAnimals[animals[result]]
                            scope.launch { sheetState.show() }
                            sheetDisplayingHeight = 0
                        }
                    }

                    LaunchedEffect(lensFacing) {
                        val cameraProvider = context.getCameraProvider()

                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleCurrentOwner,
                            cameraSelector,
                            preview,
                            analysis
                        )

                        val cameraInfo = camera.cameraInfo
                        val cameraControl = camera.cameraControl

                        // Listen to pinch gestures
                        val listener =
                            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                                override fun onScale(detector: ScaleGestureDetector): Boolean {
                                    // Get the camera's current zoom ratio
                                    currentZoomRatio =
                                        cameraInfo.zoomState.value?.zoomRatio ?: 0F

                                    // Get the pinch gesture's scaling factor
                                    delta = detector.scaleFactor
                                    // Update the camera's zoom ratio. This is an asynchronous operation that returns
                                    // a ListenableFuture, allowing you to listen to when the operation completes.
                                    cameraControl.setZoomRatio(currentZoomRatio * delta)

                                    // Return true, as the event was handled
                                    return true
                                }
                            }
                        val scaleGestureDetector = ScaleGestureDetector(context, listener)

                        // Attach the pinch gesture listener to the viewfinder
                        previewView.setOnTouchListener { _, event ->
                            scaleGestureDetector.onTouchEvent(event)
                            return@setOnTouchListener true
                        }

                        preview.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
                        recognitionRunning = CameraUi().cameraUi(recognitionRunning, navController)
                    }
                } else {
                    CameraUi().NoCameraPermissionUi(
                        navController,
                        permissions.permissions[0].status.isGranted,
                        permissions.permissions[1].status.isGranted,
                    )
                }
            }
        }
    }
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}
