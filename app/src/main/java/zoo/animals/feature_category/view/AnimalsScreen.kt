package zoo.animals.feature_category.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.*
import zoo.animals.R
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.navigation.Routes
import zoo.animals.shared.TopBar


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
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
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