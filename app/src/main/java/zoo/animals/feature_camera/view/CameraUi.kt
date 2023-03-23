package zoo.animals.feature_camera.view

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_camera.CameraExtensions
import zoo.animals.feature_camera.model.CameraViewModel
import zoo.animals.feature_camera.model.CaptureViewModel
import zoo.animals.shared.TopBar
import java.math.RoundingMode
import kotlin.time.Duration.Companion.seconds


@Composable
fun CameraUi(
    navController: NavController,
    cameraViewModel: CameraViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    captureViewModel: CaptureViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    var imageWasDeleted by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){

        // TOP UI - Home button and captured image
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            BtnClose(navController, Modifier.padding(20.dp))

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ){ ShowCapturedImage(captureViewModel.state.imageUri.value) { imageWasDeleted = it } }

            LaunchedEffect(captureViewModel.state.imageUri.value, imageWasDeleted) {
                if (!imageWasDeleted)
                    delay(3.seconds)

                captureViewModel.state.imageUri.value = null
                imageWasDeleted = false
            }
        }

        // BOTTOM UI - Image capture button and classification running toggle button and its notice
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetectionNotice(cameraViewModel.state.classificationRunning.value)

            Row{
                Box(contentAlignment = Alignment.Center) {
                    Box{
                        BtnCapture(cameraViewModel, captureViewModel)
                    }
                    Box(Modifier.offset(x = 80.dp)){
                        BtnRecognitionToggle()
                    }
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ){
                    Slider(
                        value = cameraViewModel.state.currentZoom.value,
                        valueRange = 1f..10f,
                        onValueChange = {
                            cameraViewModel.state.camera.value?.cameraControl?.setZoomRatio(it)
                            cameraViewModel.state.currentZoom.value = it
                        },
                        steps = 9,
                        enabled = true,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                            activeTickColor = MaterialTheme.colorScheme.onError,
                            inactiveTickColor = MaterialTheme.colorScheme.surfaceTint
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "x${cameraViewModel.state.currentZoom.value.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)}",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun NoCameraPermissionUi(
    navController: NavController,
    missingCameraPermission: Boolean,
    missingStoragePermission: Boolean
) {
    TopBar(title = "", navController = navController, showSearch = false, showBackBtn = true) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                ) {
                    Text(
                        text = UiTexts.ArrayResource(R.array.missingPermissions, 0).asString(),
                        fontSize = 50.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Column {
                        Text(
                            text = UiTexts.ArrayResource(R.array.missingPermissions, 1).asString(),
                            fontSize = 25.sp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .padding(top = 14.dp)
                        ){

                            if (!missingCameraPermission){
                                PermissionMissingIcon(
                                    permissionName = UiTexts.ArrayResource(
                                        R.array.permissions,
                                        0
                                    ).asString(),
                                    image = Icons.Filled.Camera
                                )
                            }
                            if (!missingStoragePermission){
                                PermissionMissingIcon(
                                    permissionName = UiTexts.ArrayResource(
                                        R.array.permissions,
                                        1
                                    ).asString(),
                                    image = Icons.Filled.SdStorage
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally){
                            Text(
                                text = UiTexts.ArrayResource(R.array.missingPermissions, 2).asString(),
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            val context = LocalContext.current
                            LargeFloatingActionButton(onClick = {
                                context.startActivity(
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName,null)
                                    )
                                )
                            }) {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = UiTexts.StringResource(R.string.settings).asString(),
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetectionNotice(running: Boolean){
    ContentAnimation().FadeInFromVerticallySide(offsetY = 300, duration = 200, !running)
    {
        Row(Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
            Box(modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(10.dp))
            ){

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = UiTexts.StringResource(R.string.detectionStoppedNotice).asString(),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Composable
fun ShowCapturedImage(
    uri: Uri?,
    imageWasDeleted: (Boolean) -> Unit
){
    val context = LocalContext.current

    ContentAnimation().FadeInFromVerticallySide(offsetY = -500, duration = 400, (uri != null)){
        Box(
            Modifier
                .width(100.dp)
                .height(140.dp)
                .border(BorderStroke(3.dp, Color.Gray), RoundedCornerShape(10.dp))
        ){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ){
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxSize()
                )
            }

            Box(modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ){
                IconButton(
                    onClick = {
                        if (uri != null) {
                            CameraExtensions().deleteImage(context, uri) { imageWasDeleted(it) }
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(1.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
