package zoo.animals.feature_discovery.animals

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import zoo.animals.feature_discovery.zoos.SwipeableActionsBox
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_discovery.animals.data.AnimalsDiscoveryScreenViewModel
import zoo.animals.feature_discovery.swipeActionEnd
import zoo.animals.feature_discovery.swipeActionStart
import zoo.animals.navigation.Routes
import zoo.animals.shared.TopBar
import zoo.animals.stringMapToIndexKey

class AnimalsDiscoveryScreen {

    @Composable
    fun MammalsDiscoveryScreen(navController: NavController, animalsViewMode: AnimalsDiscoveryScreenViewModel = viewModel()){
        AnimalsDiscoveryScreen(navController = navController, id = 0, animalsViewModel = animalsViewMode)
    }

    @Composable
    fun BirdsDiscoveryScreen(navController: NavController, animalsViewMode: AnimalsDiscoveryScreenViewModel = viewModel()){
        AnimalsDiscoveryScreen(navController = navController, id = 1, animalsViewModel = animalsViewMode)
    }

    @Composable
    fun ReptilesDiscoveryScreen(navController: NavController, animalsViewMode: AnimalsDiscoveryScreenViewModel = viewModel()){
        AnimalsDiscoveryScreen(navController = navController, id = 2, animalsViewModel = animalsViewMode)
    }


    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("NotConstructor")
    @Composable
    fun AnimalsDiscoveryScreen(navController: NavController, id: Int, animalsViewModel: AnimalsDiscoveryScreenViewModel){
        val showAllText = UiTexts.StringResource(R.string.showAll).asString()
        val showSeenText = UiTexts.ArrayResource(R.array.showDiscovered,1).asString()
        val showNotSeenText = UiTexts.ArrayResource(R.array.showNotDiscovered, 1).asString()
        val radioOptions = listOf(
            showAllText,
            showSeenText,
            showNotSeenText,
        )
        val (selectedOption, onOptionSelected) = rememberSaveable { mutableStateOf(radioOptions[0]) }

        var state by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = state){
            state = false
        }

        TopBar(
            title = UiTexts.StringResource(R.string.animals).asString(),
            navController = navController,
            animalKey = null, zooKey = null
        ){
            LazyColumn(
                content = {
                    stickyHeader{
                        Row(
                            Modifier
                                .selectableGroup()
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            radioOptions.forEach { text ->
                                Row(
                                    Modifier
                                        .padding(vertical = 8.dp, horizontal = 16.dp)
                                        .selectable(
                                            selected = (text == selectedOption),
                                            onClick = { onOptionSelected(text) },
                                            role = Role.RadioButton
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (text == selectedOption),
                                        onClick = null
                                    )
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    }

                    when (selectedOption) {
                        showSeenText -> {
                            val animals = animalsViewModel.getSeen(id)
                            val keys = stringMapToIndexKey(animals)
                            items(animals.size){ i ->
                                animals[keys[i]]?.let {
                                    AnimalCard(
                                        animal = it,
                                        navController = navController,
                                        animalKey = keys[i],
                                        animalCategory = id,
                                        animalsViewModel = animalsViewModel,
                                        StateChange = { ch -> state = ch }
                                    )
                                }
                            }
                        }
                        showNotSeenText -> {
                            val animals = animalsViewModel.getNotSeen(id)
                            val keys = stringMapToIndexKey(animals)
                            items(animals.size){ i ->
                                animals[keys[i]]?.let {
                                    AnimalCard(
                                        animal = it,
                                        navController = navController,
                                        animalKey = keys[i],
                                        animalCategory = id,
                                        animalsViewModel = animalsViewModel,
                                        StateChange = { ch -> state = ch }
                                    )
                                }
                            }
                        }
                        else -> {
                            val animals = animalsViewModel.getAll(id)
                            val keys = stringMapToIndexKey(animals)
                            items(animals.size){ i ->
                                animals[keys[i]]?.let {
                                    AnimalCard(
                                        animal = it,
                                        navController = navController,
                                        animalKey = keys[i],
                                        animalCategory = id,
                                        animalsViewModel = animalsViewModel,
                                        StateChange = { ch -> state = ch }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }


    @Composable
    fun AnimalCard(
        animal: Animal,
        navController: NavController,
        animalKey: String?,
        animalCategory: Int,
        animalsViewModel: AnimalsDiscoveryScreenViewModel,
        StateChange: (Boolean) -> Unit
    ){
        var seen by remember(animal) { mutableStateOf(animal.seen) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val saveAction = swipeActionStart{
            scope.launch {
                animalKey?.let { AnimalData.saveSeenAnimal(it, context) }
            }
            animalsViewModel.animals[animalCategory][animalKey]?.seen = true
            StateChange(true)
            seen = true
        }
        val deleteAction = swipeActionEnd{
            scope.launch {
                animalKey?.let { AnimalData.removeFromSeenAnimal(it, context) }
            }
            animalsViewModel.animals[animalCategory][animalKey]?.seen = false
            StateChange(true)
            seen = false
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            SwipeableActionsBox(
                startActions = listOf(saveAction),
                endActions = listOf(deleteAction),
                swipeThreshold = 100.dp,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Card(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .clickable(
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "animalData",
                                    animal
                                )
                                navController.navigate(Routes.AnimalInfo.route)
                            }
                        ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Box(Modifier.fillMaxSize()) {
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
                                    painter = rememberAsyncImagePainter(animal.previewImage),
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
                                Box {
                                    if (seen) {
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.TopEnd
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.checkmark),
                                                contentDescription = ""
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.65f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = animal.name,
                                            textAlign = TextAlign.Center,
                                            fontSize = 36.sp,
                                            lineHeight = 36.sp,
                                            maxLines = 2
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
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
}