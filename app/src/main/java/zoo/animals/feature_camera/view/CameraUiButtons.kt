package zoo.animals.feature_camera.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.feature_camera.model.CameraViewModel
import zoo.animals.feature_camera.model.CaptureViewModel
import zoo.animals.navigation.Routes


@Composable
fun BtnRecognitionToggle(
    viewModel: CameraViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val interactionSource = MutableInteractionSource()
    Box(
        modifier = Modifier
            .then(Modifier.size(40.dp))
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                viewModel.state.classificationRunning.value = !viewModel.state.classificationRunning.value
            }
    ) {
        Crossfade(
            targetState = viewModel.state.classificationRunning.value,
            animationSpec = TweenSpec(durationMillis = 500)
        ) { isChecked ->
            if (isChecked) {
                Icon(
                    painter = painterResource(id = R.drawable.eye_open),
                    contentDescription = "DetectionToggleButton",
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.eye_closed),
                    contentDescription = "DetectionToggleButton",
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun BtnCapture(
    cameraViewModel: CameraViewModel,
    captureViewModel: CaptureViewModel
){
    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            captureViewModel.takePhoto(cameraViewModel, context)
        },
        modifier= Modifier.size(70.dp),
        shape = CircleShape,
        border= BorderStroke(5.dp, MaterialTheme.colorScheme.surfaceVariant),
        contentPadding = PaddingValues(5.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurfaceVariant),
            contentAlignment = Alignment.Center
        ){
            Box(modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxSize(0.9f)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ){
                    Icon(
                        Icons.Filled.PhotoCamera,
                        "ImageCaptureButton",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Composable
fun BtnClose(navController: NavController, modifier: Modifier){
    Box(
        modifier = modifier
    ){
        IconButton(
            onClick = {
                navController.navigate(Routes.Categories.route)
            },
            modifier = Modifier
                .then(Modifier.size(40.dp))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "DetectionToggleButton",
                modifier = Modifier.fillMaxSize(),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun PermissionMissingIcon(permissionName: String, image: ImageVector){
    Box(modifier = Modifier
        .clip(FloatingActionButtonDefaults.largeShape)
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(
                image,
                permissionName,
                Modifier
                    .size(100.dp)
                    .padding(bottom = 0.dp)
                    .align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                permissionName,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom=10.dp)
            )
        }
    }
}



/*
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionSlider(permission: String, columnHeight: Dp){
    var sliderOffsetY by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    var sliderHeight by remember { mutableStateOf(0.dp) }

    var inSettings by remember { mutableStateOf(false) }
    var columnThreshold by remember { mutableStateOf(0.dp) }
    var stickFromThreshold by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Green,
                        Color.Red
                    )
                )
            )
            .height(columnHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(modifier = Modifier
            .offset {
                IntOffset(
                    0,
                    with(localDensity) {
                        sliderOffsetY
                            .toPx()
                            .toInt()
                    })
            }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    sliderOffsetY += with(localDensity) { delta.toDp() }
                },
                onDragStopped = {
                    columnThreshold =
                        ((columnHeight / 2) - (sliderHeight / 2)).value.roundToInt().dp
                    stickFromThreshold =
                        (columnThreshold - (columnThreshold / 3)).value.roundToInt().dp

                    sliderOffsetY =
                        if (sliderOffsetY >= -stickFromThreshold && sliderOffsetY <= stickFromThreshold) {
                            0.dp
                        } else if (sliderOffsetY < -stickFromThreshold) {
                            -columnThreshold
                        } else {
                            columnThreshold
                        }
                }
            )
        ) {
            Box(Modifier.clip(CircleShape)){
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .onGloballyPositioned {
                            sliderHeight = with(localDensity) { it.size.height.toDp() }
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Filled.Camera,
                            permission,
                            Modifier
                                .size(100.dp)
                                .padding(bottom = 0.dp)
                                .align(Alignment.CenterHorizontally),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            permission,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom=10.dp)
                        )
                    }
                }
            }
        }
    }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (sliderOffsetY <= -columnThreshold){
        sliderOffsetY = -columnThreshold
        if (!cameraPermissionState.status.isGranted && !inSettings){
            inSettings = true
            LocalContext.current.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", LocalContext.current.packageName,null)
                )
            )
        }
    } else{
        inSettings = false
    }
    if (sliderOffsetY >= columnThreshold){
        sliderOffsetY = columnThreshold
    }

//    Text(text = "LALA: " + sliderOffsetY + "  " + sliderHeight + "  " + columnThreshold + "  " + stickFromThreshold)

//    var inSettings by remember { mutableStateOf(false) }
//    if (offsetY <= -((colHeight/2)-30.dp).value.dp){
//        offsetY = (-(colHeight/2)-30.dp).value.dp
//        if (!cameraPermissionState.status.isGranted && !inSettings){
//            inSettings = true
//            LocalContext.current.startActivity(
//                Intent(
//                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.fromParts("package", LocalContext.current.packageName,null)
//                )
//            )
//        }
//    }
//    if (offsetY >= ((colHeight/2)+30.dp).value.dp){
//        offsetY = ((colHeight/2)+30.dp).value.dp
//        inSettings = false
//    }
}*/
