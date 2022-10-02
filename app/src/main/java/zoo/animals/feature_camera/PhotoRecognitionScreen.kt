package zoo.animals.feature_camera

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import zoo.animals.R
import zoo.animals.navigation.Routes
import zoo.animals.UiTexts
import zoo.animals.feature_camera.CameraUtils.uriToBitmap
import zoo.animals.feature_category.data.Animal
import zoo.animals.shared.TopBar


@Composable
fun PhotoRecognitionScreen(navController: NavController){
    TopBar(
        title = UiTexts.StringResource(R.string.empty).asString(),
        navController = navController,
        showSearch = false
    ) {
        Box(
            Modifier
                .fillMaxHeight(0.932f)
                .fillMaxWidth()
        ){
            var imageUri by remember {
                mutableStateOf<Uri?>(null)
            }
            var bitmap by remember {
                mutableStateOf<Bitmap?>(null)
            }
            val context = LocalContext.current
            var animal by remember { mutableStateOf<Animal?>(Animal("","", emptyMap(), "", 0,0)) }
            
            val launcher = rememberLauncherForActivityResult(contract =
            ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    imageUri = uri
                }
            }

            var counter by remember { mutableStateOf(0) }

            Column(
                Modifier
                    .fillMaxSize()
            ){
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.65f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ){
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // To prevent Bitmap updating
                LaunchedEffect(key1 = imageUri){
                    counter++
                    bitmap = imageUri?.let { uriToBitmap(it, context) }
                    animal = bitmap?.let { ImageClassifier(context).classifyPhoto(it) }
                }

                Column(
                    Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (counter == 1){
                            Text(
                                UiTexts.StringResource(R.string.selectImage).asString(),
                                textAlign = TextAlign.Center,
                            )
                        } else{
                            when (animal) {
                                null -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = UiTexts.StringResource(R.string.animalNotRecognized1).asString(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 25.sp,
                                        )
                                        Text(
                                            UiTexts.StringResource(R.string.animalNotRecognized2).asString(),
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                                else -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(UiTexts.StringResource(R.string.animalRecognized).asString())
                                        Text(
                                            text = animal!!.name,
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = TextDecoration.Underline,
                                            color = Color.Blue,
                                            modifier = Modifier
                                                .clickable(onClick = {
                                                    navController.currentBackStackEntry?.savedStateHandle?.set("animalData", animal)
                                                    navController.navigate(Routes.AnimalInfo.route) })
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxHeight(0.2f)
                    ){
                        Button(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier
                                .fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Text(
                                UiTexts.StringResource(R.string.openGallery).asString(),
                                fontSize = 25.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

