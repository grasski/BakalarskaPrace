package zoo.animals.feature_discovery.zoos.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.navigation.Routes
import zoo.animals.UiTexts
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.shared.TopBar


@Composable
fun DiscoveryScreen(navController: NavController){

    TopBar(title = UiTexts.StringResource(R.string.discoveries).asString(), navController = navController)
    {
        LazyColumn(){
            item{
                LineTextDivider(UiTexts.StringResource(R.string.zoos).asString())

                val zoosCount by remember { mutableStateOf(ZooData.allZoosInstance.size.toFloat()) }
                val zoosVisited = remember { mutableStateOf(ZooData.allZoosInstance.count { it.visited }.toFloat()) }
                DiscoveryCard(
                    navController = navController,
                    header = UiTexts.StringResource(R.string.zoos).asString(),
                    route = Routes.Zoos,
                    zoosCount,
                    zoosVisited,
                ""
                )
            }

            item{
                LineTextDivider(UiTexts.StringResource(R.string.animals).asString())

                val mammalsCount by remember { mutableStateOf(AnimalData.allAnimalsInstance[0].size.toFloat()) }
                val mammalsSeen = remember { mutableStateOf(0f) }
                val birdsCount by remember { mutableStateOf(AnimalData.allAnimalsInstance[1].size.toFloat()) }
                val birdsSeen = remember { mutableStateOf(0f) }
                val reptilesCount by remember { mutableStateOf(AnimalData.allAnimalsInstance[2].size.toFloat()) }
                val reptilesSeen = remember { mutableStateOf(0f) }

                DiscoveryCard(
                    navController = navController,
                    header = UiTexts.ArrayResource(R.array.mammalsCategory,0).asString(),
                    route = Routes.AnimalsDiscovery,
                    mammalsCount,
                    mammalsSeen,
                    "mammals"
                )
                DiscoveryCard(
                    navController = navController,
                    header = UiTexts.ArrayResource(R.array.birdsCategory,0).asString(),
                    route = Routes.AnimalsDiscovery,
                    birdsCount,
                    birdsSeen,
                    "birds"
                )
                DiscoveryCard(
                    navController = navController,
                    header = UiTexts.ArrayResource(R.array.reptilesCategory,0).asString(),
                    route = Routes.AnimalsDiscovery,
                    reptilesCount,
                    reptilesSeen,
                    "reptiles"
                )
            }
        }
    }
}


@Composable
fun LineTextDivider(text: String) {
    Box(
        Modifier.fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ){
        Divider()
        Text(
            text = text,
            fontSize = 18.sp,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 5.dp, end = 5.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryCard(
    navController: NavController,
    header: String,
    route: Routes,
    itemCount: Float,
    itemsSeen: MutableState<Float>,
    categoryData: String
) {
    val itemsVisited= itemsSeen.value
    var starter by rememberSaveable { mutableStateOf(false) }
    val itemsVisitedAnimation: Float by animateFloatAsState(
        if (starter) itemsVisited else 0f,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit){
        scope.launch {
            delay(200)
            starter = true
        }
    }

    Box(Modifier.fillMaxSize()){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                when(categoryData){
                    "mammals" -> navController.currentBackStackEntry?.savedStateHandle?.set("animalsData", AnimalData.allAnimalsInstance[0])
                    "birds" -> navController.currentBackStackEntry?.savedStateHandle?.set("animalsData", AnimalData.allAnimalsInstance[1])
                    "reptiles" -> navController.currentBackStackEntry?.savedStateHandle?.set("animalsData", AnimalData.allAnimalsInstance[2])
                }

                navController.navigate(route.route)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    header,
                    fontSize = 25.sp
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Slider(
                            value = itemsVisitedAnimation / itemCount,
                            onValueChange = { },
                            steps = itemCount.toInt() - 1, // 13 = 14 items
                            enabled = false,
                            colors = SliderDefaults.colors(
                                disabledThumbColor = Color.Transparent,
                                disabledActiveTrackColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                disabledInactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                                disabledActiveTickColor = MaterialTheme.colorScheme.onError,
                                disabledInactiveTickColor = MaterialTheme.colorScheme.surfaceTint
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight()
                        )
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                "${itemsVisited.toInt()}/${itemCount.toInt()}",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
