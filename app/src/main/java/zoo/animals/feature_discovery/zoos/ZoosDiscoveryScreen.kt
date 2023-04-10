package zoo.animals.feature_discovery.zoos

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_discovery.swipeActionEnd
import zoo.animals.feature_discovery.swipeActionStart
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.feature_discovery.zoos.data.ZoosDiscoveryScreenViewModel
import zoo.animals.navigation.Routes
import zoo.animals.shared.TopBar
import zoo.animals.stringMapToIndexKey

class ZoosDiscoveryScreen {

    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("NotConstructor", "UnrememberedMutableState")
    @Composable
    fun ZoosDiscoveryScreen(navController: NavController, zoosViewModel: ZoosDiscoveryScreenViewModel = viewModel()){
        val showAllText = UiTexts.StringResource(R.string.showAll).asString()
        val showDiscoveredText = UiTexts.ArrayResource(R.array.showDiscovered,0).asString()
        val showNotDiscoveredText = UiTexts.ArrayResource(R.array.showNotDiscovered, 0).asString()
        val radioOptions = listOf(
            showAllText,
            showDiscoveredText,
            showNotDiscoveredText,
        )
        val (selectedOption, onOptionSelected) = rememberSaveable { mutableStateOf(radioOptions[0]) }

        var state by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = state){
            state = false
        }

        TopBar(
            title = UiTexts.StringResource(R.string.zoos).asString(),
            navController = navController,
        ) {
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
                                        modifier = Modifier.padding(start = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    when (selectedOption) {
                        showDiscoveredText -> {
                            val zoos = zoosViewModel.getVisited()
                            val keys = stringMapToIndexKey(zoos)
                            items(zoos.size){ i ->
                                zoos[keys[i]]?.let {
                                    ZooCard(
                                        zoo = it,
                                        navController = navController,
                                        zooKey = keys[i],
                                        viewModel = zoosViewModel,
                                        StateChange = { ch -> state = ch }
                                    )
                                }
                            }
                        }
                        showNotDiscoveredText -> {
                            val zoos = zoosViewModel.getNotVisited()
                            val keys = stringMapToIndexKey(zoos)
                            items(zoos.size){ i ->
                                zoos[keys[i]]?.let {
                                    ZooCard(
                                        zoo = it,
                                        navController = navController,
                                        zooKey = keys[i],
                                        viewModel = zoosViewModel,
                                        StateChange = { ch -> state = ch }
                                    )
                                }
                            }
                        }
                        else -> {
                            val zoos = zoosViewModel.getAll()
                            val keys = stringMapToIndexKey(zoos)
                            items(zoos.size){ i ->
                                zoos[keys[i]]?.let {
                                    ZooCard(
                                        zoo = it,
                                        navController = navController,
                                        zooKey = keys[i],
                                        viewModel = zoosViewModel,
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
    fun ZooCard(
        zoo: Zoo,
        navController: NavController,
        zooKey: String?,
        viewModel: ZoosDiscoveryScreenViewModel,
        StateChange: (Boolean) -> Unit
    ){
        var visited by remember(zoo) { mutableStateOf(zoo.visited) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val saveAction = swipeActionStart{
            scope.launch {
                zooKey?.let { ZooData.saveVisitedZoo(it, context) }
            }
            viewModel.zoos[zooKey]?.visited = true
            StateChange(true)
            visited = true
        }
        val deleteAction = swipeActionEnd{
            scope.launch {
                zooKey?.let { ZooData.removeFromVisitedZoo(it, context) }
            }
            viewModel.zoos[zooKey]?.visited = false
            StateChange(true)
            visited = false
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
                                    "zooData",
                                    zoo
                                )
                                navController.navigate(Routes.ZooInfo.route)
                            }
                        ),
                    shape = RoundedCornerShape(12.dp)
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
                                    painter = rememberAsyncImagePainter(zoo.logo),
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
                                Box{
                                    if (visited){
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
                                            text = zoo.city,
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
                                        text = zoo.type,
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