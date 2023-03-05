package zoo.animals.feature_discovery.zoos.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.feature_discovery.zoos.data.ZoosDiscoveryScreenViewModel
import zoo.animals.navigation.Routes
import zoo.animals.shared.TopBar

class ZoosDiscoveryScreen {

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
                    item{
                        Row(
                            Modifier
                                .selectableGroup()
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            radioOptions.forEach { text ->
                                Row(
                                    Modifier
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
                        showDiscoveredText -> {
                            items(zoosViewModel.getVisited().size){ i ->
                                ZooCard(zoosViewModel.getVisited()[i], navController, zoosViewModel) {
                                    state = it
                                }
                            }
                        }
                        showNotDiscoveredText -> {
                            items(zoosViewModel.getNotVisited().size){ i ->
                                ZooCard(zoosViewModel.getNotVisited()[i], navController, zoosViewModel) {
                                    state = it
                                }
                            }
                        }
                        else -> {
                            items(zoosViewModel.getAll().size) { i ->
                                ZooCard(zoosViewModel.getAll()[i], navController, zoosViewModel) {
                                    state = it
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ZooCard(zoo: Zoo, navController: NavController, viewModel: ZoosDiscoveryScreenViewModel, StateChange: (Boolean) -> Unit){
        var visited by remember(zoo) { mutableStateOf(zoo.visited) }
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
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "zooData",
                                zoo
                            )
                            navController.navigate(Routes.ZooInfo.route)
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

        if (visited){
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val removeZoo = remember { UiTexts.StringResource(R.string.removeZoo).asString(context) }
            val removeText = remember { UiTexts.StringResource(R.string.removeFromDiscovery, removeZoo, zoo.city).asString(context) }

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
                                                ZooData.removeFromVisitedZoo(zoo.city, context)
                                            }
                                            viewModel.zoos.firstOrNull { it.city == zoo.city }?.visited = false
                                            StateChange(true)

                                            visited = false
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