package zoo.animals.camera

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.jetbrains.annotations.Nullable
import zoo.animals.R
import zoo.animals.Routes
import zoo.animals.TopBar
import zoo.animals.UiTexts
import zoo.animals.camera.CameraUtils.uriToBitmap
import zoo.animals.data.Animal


@Composable
fun PhotoRecognitionScreen(navController: NavController){
    TopBar(
        title = UiTexts.StringResource(R.string.empty).asString(),
        navController = navController,
        showSearch = false
    ) {
        Box(
            Modifier
                .fillMaxSize()
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
                    .fillMaxHeight(0.932f)
                    .fillMaxWidth()
            ){
                Box(contentAlignment = Alignment.TopCenter){
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        "",
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth(),
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
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if (counter == 1){
                        Text(
                            UiTexts.StringResource(R.string.selectImage).asString(),
                            textAlign = TextAlign.Center,
                        )
                    } else{
                        when (animal) {
                            null -> {
                                Text(
                                    text = UiTexts.StringResource(R.string.animalNotRecognized1).asString(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp
                                )
                                Text(
                                    UiTexts.StringResource(R.string.animalNotRecognized2).asString(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            else -> {
                                Text(UiTexts.StringResource(R.string.animalRecognized).asString())
                                Text(
                                    text = animal!!.name,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline,
                                    color = Color.Blue,
                                    modifier = Modifier.clickable(onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("animalData", animal)
                                        navController.navigate(Routes.AnimalInfo.route)
                                    })
                                )
                            }
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                ){
                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
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

