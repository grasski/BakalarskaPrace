package zoo.animals.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
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
import zoo.animals.Routes
import zoo.animals.TopBar
import zoo.animals.UiTexts
import zoo.animals.data.ZooData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(navController: NavController){
    val zoosCount = 14f
    val zoosVisited by remember { mutableStateOf(ZooData.allZoosInstance.count { it.visited }.toFloat()) }
    var starter by rememberSaveable { mutableStateOf(false) }

    val zoosVisitedAnimation: Float by animateFloatAsState(
        if (starter) zoosVisited else 0f,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit){
        scope.launch {
            delay(200)
            starter = true
        }
    }


    TopBar(title = UiTexts.StringResource(R.string.discoveries).asString(), navController = navController)
    {
        Box(Modifier.fillMaxSize()){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { navController.navigate(Routes.Zoos.route) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        UiTexts.StringResource(R.string.zoos).asString(),
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
                                value = zoosVisitedAnimation/zoosCount,
                                onValueChange = { },
                                steps = zoosCount.toInt()-1, // 13 = 14 items
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
                                    "${zoosVisited.toInt()}/${zoosCount.toInt()}",
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

