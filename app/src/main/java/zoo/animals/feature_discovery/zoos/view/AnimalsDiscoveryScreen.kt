package zoo.animals.feature_discovery.zoos.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_category.data.Animal
import zoo.animals.navigation.Routes
import zoo.animals.shared.TopBar
import zoo.animals.stringMapToIndexKey

class AnimalsDiscoveryScreen {

    @SuppressLint("NotConstructor", "CoroutineCreationDuringComposition")
    @Composable
    fun AnimalsDiscoveryScreen(navController: NavController, animalsData: MutableMap<String, Animal>){
        TopBar(
            title = UiTexts.StringResource(R.string.animals).asString(),
            navController = navController,
        ){
            val nameKeys = stringMapToIndexKey(animalsData)
            LazyColumn(
                content = {
                    items(animalsData.size){ i ->
                        animalsData[nameKeys[i]]?.let { AnimalCard(it, navController) }
                    }
                }
            )
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AnimalCard(animal: Animal, navController: NavController){
        val seen by remember(animal) { mutableStateOf(animal.seen) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Card(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("animalData", animal)
                    navController.navigate(Routes.AnimalInfo.route)
                }
            ){
                Box(Modifier.fillMaxSize()){
                    Row(
                        Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = animal.previewImage),
                                contentDescription = animal.name + "_photo",
                                modifier = Modifier.fillMaxSize(0.9f)
                            )
                        }

                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box{
                                if (seen){
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        Image(painter = painterResource(R.drawable.checkmark), contentDescription = "")
                                    }
                                }

                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.65f),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = animal.name,
                                        textAlign = TextAlign.Center,
                                        fontSize = 36.sp,
                                        lineHeight = 36.sp,
                                        maxLines = 2
                                    )
                                }
                            }

                            Box(modifier = Modifier
                                .fillMaxSize()
                            ){
                                Text(
                                    text = animal.category,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}