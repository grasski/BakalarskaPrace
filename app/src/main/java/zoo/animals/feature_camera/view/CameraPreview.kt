package zoo.animals.feature_camera.view


import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.view.ScaleGestureDetector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.feature_camera.model.CameraViewModel
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.view.CameraSheetInfo
import kotlin.math.min

/*
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
    val camExt = remember { CameraExtensions() }

    var imBitmap by remember {
        mutableStateOf(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
    }
    var recognitionRunning by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf("") }
    var currentZoomRatio by remember { mutableStateOf(1f) }
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


    var location = remember { mutableListOf(0f, 0f, 0f, 0f) }
    var switch by remember { mutableStateOf(true) }

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
                Text("Zvire: " + result)
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

                    val imageCapture = remember {
                        ImageCapture.Builder()
                            .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                            .setJpegQuality(100)
//                            .setTargetRotation(previewView.display.rotation)
                            .build()
                    }

                    val imageAnalysis = remember {
                        ImageAnalysis.Builder()
//                            .setTargetRotation(previewView.display.rotation)
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
                            if (switch){
                                imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                    val classified = ImageClassifier(context).classify(imageProxy)
                                    tempResult = classified[0] as String
                                    imBitmap = classified[1] as Bitmap?
//                                location = classified[2] as MutableList<Float>
                                }
                            } else{
                                imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                    val detected = ImageClassifier(context).detect(imageProxy)
                                    tempResult = detected[0].toString()
                                    location = detected[1] as MutableList<Float>
                                }
                            }


                        } else {
                            Image(
                                bitmap = imBitmap.asImageBitmap(),
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.TopCenter
                            )
                            imageAnalysis.clearAnalyzer()
                        }
                    } else{
                        imageAnalysis.clearAnalyzer()
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
                            imageCapture,
                            imageAnalysis
                        )

                        val cameraInfo = camera.cameraInfo
                        val cameraControl = camera.cameraControl

                        // Listen to pinch gestures
                        val listener =
                            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                                override fun onScale(detector: ScaleGestureDetector): Boolean {
                                    currentZoomRatio = cameraInfo.zoomState.value?.zoomRatio ?: 0F
                                    delta = detector.scaleFactor
                                    cameraControl.setZoomRatio(currentZoomRatio * delta)

                                    return true
                                }
                            }
                        val scaleGestureDetector = ScaleGestureDetector(context, listener)
                        previewView.setOnTouchListener { _, event ->
                            scaleGestureDetector.onTouchEvent(event)
                            return@setOnTouchListener true
                        }

                        preview.setSurfaceProvider(previewView.surfaceProvider)
                    }


                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AndroidView(
                            { previewView },
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.92f)
                                .clip(RoundedCornerShape(bottomEnd=20.dp, bottomStart=20.dp))
                        )

                        val padLeft = if (location[0] > 0) {location[0].dp} else { 0.dp}
                        val padTop = if (location[1] > 0) {location[1].dp} else { 0.dp}
                        Box(
                            Modifier
                                .fillMaxSize()
//                            .offset(x = padLeft, y = padTop)
                                .padding(start = padLeft, top = padTop)
                        ){
                            Box(modifier = Modifier
                                .border(5.dp, Color.Red)
                                .width((location[2] - location[0]).dp)
                                .height((location[3] - location[1]).dp)
                            ){}
                        }

                        var imageUri: Uri? by remember { mutableStateOf(null) }

                        var model by remember { mutableStateOf("") }
                        model = if (switch) "classification" else "detection"
                        Text(model + " is active.")
                        Box(Modifier.size(80.dp).scale(2f)){
                            Switch(
                                checked = switch,
                                onCheckedChange = {
                                    switch = !switch
                                    location = mutableListOf(0f, 0f, 0f, 0f)
                                                  },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        cameraUi(
                            navController,
                            takePhoto = {
                                camExt.takePhoto(
                                    imageCapture,
                                    cameraExecutor,
                                    context,
                                    onImageCaptured = { imageUri= it },
                                    onError = {}
                                )
                            },
                            uri = imageUri
                        )
                        recognitionRunning = RecognitionRunning.status
                    }
                } else {
                    NoCameraPermissionUi(
                        navController,
                        permissions.permissions[0].status.isGranted,
                        permissions.permissions[1].status.isGranted,
                    )
                }
            }
        }
    }
}
*/


/*
@SuppressLint("ClickableViewAccessibility")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreviewScreen(navController: NavController) {
    val permissions = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ))
    val camExt = remember { CameraExtensions() }

    var recognitionRunning by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf("") }
    var currentZoomRatio by remember { mutableStateOf(1f) }
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

    var location = remember { mutableListOf(0f, 0f, 0f, 0f) }
    var switch by remember { mutableStateOf(true) }

    Box{
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

                val imageCapture = remember {
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setJpegQuality(100)
//                            .setTargetRotation(previewView.display.rotation)
                        .build()
                }

                val imageAnalysis = remember {
                    ImageAnalysis.Builder()
//                            .setTargetRotation(previewView.display.rotation)
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
                    if (switch){
                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val classified = ImageClassifier(context).classify(imageProxy)
                            tempResult = classified[0] as String
                        }
                    } else{
                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            val detected = ImageClassifier(context).detect(imageProxy)
                            tempResult = detected[0].toString()
                            location = detected[1] as MutableList<Float>
                        }
                    }
                } else{
                    imageAnalysis.clearAnalyzer()
                }


                if (tempResult != "") {
                    result = tempResult
                    tempResult = ""
                    if (catalogAnimals.containsKey(animals[result])){
                        animal = catalogAnimals[animals[result]]
                    }
                }

                LaunchedEffect(lensFacing) {
                    val cameraProvider = context.getCameraProvider()

                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleCurrentOwner,
                        cameraSelector,
                        preview,
                        imageCapture,
                        imageAnalysis
                    )

                    val cameraInfo = camera.cameraInfo
                    val cameraControl = camera.cameraControl

                    // Listen to pinch gestures
                    val listener =
                        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                            override fun onScale(detector: ScaleGestureDetector): Boolean {
                                currentZoomRatio = cameraInfo.zoomState.value?.zoomRatio ?: 0F
                                delta = detector.scaleFactor
                                cameraControl.setZoomRatio(currentZoomRatio * delta)

                                return true
                            }
                        }
                    val scaleGestureDetector = ScaleGestureDetector(context, listener)
                    previewView.setOnTouchListener { _, event ->
                        scaleGestureDetector.onTouchEvent(event)
                        return@setOnTouchListener true
                    }

                    preview.setSurfaceProvider(previewView.surfaceProvider)
                }


                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AndroidView(
                        { previewView },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.932f)
                            .background(MaterialTheme.colorScheme.background)
                            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    )

                    val padLeft = if (location[0] > 0) {location[0].dp} else { 0.dp}
                    val padTop = if (location[1] > 0) {location[1].dp} else { 0.dp}
                    Box(
                        Modifier
                            .fillMaxSize()
//                            .offset(x = padLeft, y = padTop)
                            .padding(start = padLeft, top = padTop)
                    ){
                        Box(modifier = Modifier
                            .border(5.dp, Color.Red)
                            .width((location[2] - location[0]).dp)
                            .height((location[3] - location[1]).dp)
                        ){}
                    }

                    var imageUri: Uri? by remember { mutableStateOf(null) }

                    var model by remember { mutableStateOf("") }
                    Column{
                        Text("Zvire: " + result)
                        model = if (switch) "classification" else "detection"
                        Text(model + " is active.")
                    }
                    Box(
                        Modifier
                            .size(80.dp)
                            .scale(1f)){
                        Switch(
                            checked = switch,
                            onCheckedChange = {
                                switch = !switch
                                location = mutableListOf(0f, 0f, 0f, 0f)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    cameraUi(
                        navController,
                        takePhoto = {
                            camExt.takePhoto(
                                imageCapture,
                                cameraExecutor,
                                context,
                                onImageCaptured = { imageUri= it },
                                onError = {}
                            )
                        },
                        uri = imageUri
                    )
                    recognitionRunning = RecognitionRunning.status
                }
            } else {
                NoCameraPermissionUi(
                    navController,
                    permissions.permissions[0].status.isGranted,
                    permissions.permissions[1].status.isGranted,
                )
            }
        }
    }
}
*/


@SuppressLint("ClickableViewAccessibility")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraPreviewScreen(
    navController: NavController,
    viewModel: CameraViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val permissions = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ))
    val context = LocalContext.current
    var zoomDelta by remember { mutableStateOf(1f) }

    var delayState by remember { mutableStateOf(true) }
    var delayedAnimal by remember { mutableStateOf<Animal?>(null)}


    Box(Modifier.fillMaxSize()){
        Column {
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
                val preview: Preview = Preview.Builder().build()
                val previewView = remember { PreviewView(context) }

                LaunchedEffect(viewModel.state.lensFacing) {
                    viewModel.setupCamera(context, lifecycleOwner, preview)

                    // Listen to pinch gestures
                    val listener =
                        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                            override fun onScale(detector: ScaleGestureDetector): Boolean {
                                viewModel.state.currentZoom.value = viewModel.state.camera.value?.cameraInfo?.zoomState?.value?.zoomRatio ?: 0F
                                zoomDelta = detector.scaleFactor
                                viewModel.state.camera.value?.cameraControl?.setZoomRatio(viewModel.state.currentZoom.value * zoomDelta)

                                return true
                            }
                        }
                    val scaleGestureDetector = ScaleGestureDetector(context, listener)
                    previewView.setOnTouchListener { _, event ->
                        scaleGestureDetector.onTouchEvent(event)
                        return@setOnTouchListener true
                    }

                    preview.setSurfaceProvider(previewView.surfaceProvider)
                }

                LaunchedEffect(viewModel.state.classificationRunning.value, viewModel.state.bottomSheetActive.value){
                    viewModel.state.classifiedAnimal.value = null
                    delayedAnimal = null
                    viewModel.analyze(viewModel.state.imageAnalysis, viewModel.state.cameraExecutor, context)
                }

                LaunchedEffect(delayState){
                    val animal = viewModel.state.classifiedAnimal.value
                    delay(800)

                    delayedAnimal = if (viewModel.state.classificationRunning.value){
                        if (viewModel.state.classifiedAnimal.value == animal) { animal } else { null }
                    } else{
                        null
                    }

                    delayState = !delayState
                }
                
//                if (animalText != null){
//                    viewModel.state.bottomSheetActive.value = true
//                    openBottomSheet = true
//
//
//                    LaunchedEffect(key1 = true){
//                        scope.launch {
//                            bottomSheetState.show()
//                        }
//                    }
//                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    previewView.let { view ->
                        AndroidView(
                            { view },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .clip(
                                    RoundedCornerShape(
                                        bottomEnd = 20.dp,
                                        bottomStart = 20.dp
                                    )
                                )
                        )
                    }

                    val scaleFactor =  min(getScreenSize().width * 1f / 640, getScreenSize().height * 1f / 640)

                    Column {
                        Text("DELAY ZVIRE: " + delayedAnimal?.name + " " + viewModel.state.bottomSheetActive.value + " " + getScreenSize())
                        Text(text = "ZVIRE: " + viewModel.state.classifiedAnimal.value?.name + " " + viewModel.state.animalCenter.value)
                    }

                    CameraUi(navController)

                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.paw_animation))
                    val progress by animateLottieCompositionAsState(
                        composition,
                        iterations = LottieConstants.IterateForever
                    )
                    if (viewModel.state.animalCenter.value.isNotNull() && delayedAnimal != null){
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.6f)
                                .align(Alignment.Center)
                                .clickable(
                                    onClick = {
                                        Log.e("", "CLICK")
                                    },
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                )
                        ){}

                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier
                                .offset(
                                    viewModel.state.animalCenter.value.x.dp / scaleFactor,
                                    viewModel.state.animalCenter.value.y.dp
                                )
                        )
                    }
                }
            } else {
                NoCameraPermissionUi(
                    navController,
                    permissions.permissions[0].status.isGranted,
                    permissions.permissions[1].status.isGranted,
                )
            }
        }
    }
}


fun Offset.isNotNull(): Boolean{
    return x != 0f && y != 0f
}
@Composable
fun getScreenSize(): Size {
    val displayMetrics = LocalContext.current.resources.displayMetrics

    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels
    return Size(width.toFloat(), height.toFloat())
}