package zoo.animals.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.Routes
import zoo.animals.TopBar
import zoo.animals.UiTexts
import zoo.animals.data.Zoo
import zoo.animals.data.ZooData


class ZoosScreen {

    @SuppressLint("NotConstructor", "CoroutineCreationDuringComposition")
    @Composable
    fun ZoosScreen(navController: NavController){
        TopBar(title = UiTexts.StringResource(R.string.zoos).asString(), navController = navController){
            val zoos = ZooData.allZoosInstance

            LazyColumn(
                content = {
                    items(zoos.size){ i ->
                        ZooCard(zoos[i], navController)
                    }
                }
            )
        }
    }
    
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ZooCard(zoo: Zoo, navController: NavController){
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var visited by remember { mutableStateOf(zoo.visited) }

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
                    navController.currentBackStackEntry?.savedStateHandle?.set("zooData", zoo)
                    navController.navigate(Routes.ZooInfo.route)
//                    scope.launch {
//                        ZooData.saveVisitedZoo(zoo.city, context)
//                    }
//                    visited = true
//                    zoo.visited = true
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
                                painter = painterResource(id = zoo.logo),
                                contentDescription = zoo.city + "_logo",
                                modifier = Modifier.fillMaxSize(0.9f)
                            )
                        }

                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.65f),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = zoo.city,
                                    textAlign = TextAlign.Center,
                                    fontSize = 36.sp,
                                    lineHeight = 36.sp,
                                    maxLines = 2
                                )
                            }

                            Box(modifier = Modifier
                                .fillMaxSize()
                            ){
                                Text(
                                    text = zoo.type,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }

                    if (!visited){
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.BottomCenter
                        ){
                            Text(
                                text = UiTexts.StringResource(R.string.discover).asString(),
                                fontSize = 40.sp,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}