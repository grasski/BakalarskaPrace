package zoo.animals.feature_discovery.zoos.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.navigation.Routes
import zoo.animals.shared.TopBar
import zoo.animals.stringMapToIndexKey
import zoo.animals.ui.theme.Shapes

class AnimalsDiscoveryScreen {

    @SuppressLint("NotConstructor")
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


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnimalCard(animal: Animal, navController: NavController){
        var seen by remember(animal) { mutableStateOf(animal.seen) }
        var longClick by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Card(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .combinedClickable(
                        onLongClick = {
                            longClick = true
                        },
                        onClick = {
                            longClick = false
                            navController.currentBackStackEntry?.savedStateHandle?.set("animalData", animal)
                            navController.navigate(Routes.AnimalInfo.route)
                        }
                    ),
                shape = RoundedCornerShape(12.dp),
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
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize(0.9f)
                                    .clip(CircleShape)
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

        if (seen){
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val removeAnimal = remember { UiTexts.StringResource(R.string.removeAnimal).asString(context) }
            val removeText = remember { UiTexts.StringResource(R.string.removeFromDiscovery, removeAnimal, animal.name).asString(context) }

            if (longClick) {
                AlertDialog(
                    onDismissRequest = {
                        longClick = false
                    },
                    confirmButton = {
                        Surface(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = removeText,
                                    fontSize = 19.sp,
                                )
                                Spacer(modifier = Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                AnimalData.removeFromSeenAnimal(animal.name, context)
                                            }
                                            seen = false
                                            longClick = false
                                        },
                                    ) {
                                        Text(
                                            UiTexts.StringResource(R.string.yes).asString(),
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(24.dp))

                                    TextButton(
                                        onClick = {
                                            longClick = false
                                        },
                                    ) {
                                        Text(
                                            UiTexts.StringResource(R.string.no).asString(),
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}