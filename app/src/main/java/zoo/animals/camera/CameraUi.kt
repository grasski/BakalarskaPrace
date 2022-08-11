package zoo.animals.camera

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.TopBar
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import java.security.Permission


class CameraUi {

    @Composable
    fun cameraUi(recognitionRunning: Boolean, navController: NavController): Boolean{
        var running by remember { mutableStateOf(recognitionRunning) }

        Box(modifier = Modifier.fillMaxSize()){
            Box(
                Modifier
                    .fillMaxSize()
            ){
                Row(
                    Modifier
                        .padding(top = 100.dp, start = 20.dp, end = 20.dp)
                ) {
                    Box(
                        Modifier
                            .padding(start = 20.dp)
                    ){
                        BtnClose(navController)
                    }
                }
            }

            Box{
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ContentAnimation().ShowsFromVerticallySide(offsetY = 300, duration = 200, !running)
                    {
                        Row(Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
                            Box(modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
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
                    
                    Row{
                        Box(contentAlignment = Alignment.Center) {
                            Box{
                                BtnCapture()
                            }
                            Box(Modifier.offset(x = 80.dp)){
                                running = btnRecognitionToggle()
                            }
                        }
                    }
                }
            }
        }

        return running
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

                        Column() {
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
                                    androidx.compose.material3.Icon(
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
}
