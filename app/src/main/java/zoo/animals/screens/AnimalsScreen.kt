package zoo.animals.screens


import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import zoo.animals.*
import zoo.animals.R
import zoo.animals.data.Animal
import zoo.animals.data.AnimalData


class CategoryAnimalsScreen {

    @Composable
    fun MammalsScreen(navController: NavController) {
        val mammals = AnimalData.allAnimalsInstance[0]

        Screen(navController, mammals, UiTexts.ArrayResource(R.array.mammalsCategory, 0).asString())
    }

    @Composable
    fun ReptilesScreen(navController: NavController) {
        val reptiles = AnimalData.allAnimalsInstance[2]

        Screen(navController, reptiles, UiTexts.ArrayResource(R.array.reptilesCategory, 0).asString())
    }

    @Composable
    fun BirdsScreen(navController: NavController) {
        val birds = AnimalData.allAnimalsInstance[1]

        Screen(navController, birds, UiTexts.ArrayResource(R.array.birdsCategory, 0).asString())
    }


    @Composable
    fun Screen(
        navController: NavController,
        animals: MutableMap<String, Animal>,
        title: String,
        showTopBar: Boolean = true
    ){
        val animalsKeys = stringMapToIndexKey(animals)
        val size = remember { animals.size }

        if (showTopBar){
            TopBar(title, navController = navController)
            {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    content = {
                        items(size){ i ->
                            PreviewAnimalCard(animal = animals[animalsKeys[i]], navController)
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        } else{
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                content = {
                    items(size){ i ->
                        PreviewAnimalCard(animal = animals[animalsKeys[i]], navController)
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PreviewAnimalCard(animal: Animal?, navController: NavController){
        animal?.let {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(180.dp)
                    .widthIn(150.dp, 180.dp),
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("animalData", animal)
                    navController.navigate(Routes.AnimalInfo.route)
                }
            ){
                Box{
                    Image(
                        painter = painterResource(it.previewImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillHeight
                    )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 350f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                    )
                    Box(modifier = Modifier.align(Alignment.BottomCenter)){
                        Text(
                            text = it.name,
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun test(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Box(modifier = Modifier
//            .fillMaxSize(0.5f)
            .align(Alignment.Center)
        ){
            Box(){
//                val img = painterResource(id = R.drawable.world_map)
//                Image(
//                    painter = img,
//                    contentDescription = null,
//                )
//                Canvas(modifier = Modifier, onDraw = {
//                    drawCircle(Color.Red, 20f, center = Offset(x = 500f, y = 0f))
//                })



            }
        }
    }
}