package zoo.animals


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.animations.ContentAnimation
import zoo.animals.data.Animal
import zoo.animals.data.AnimalData
import zoo.animals.data.CategoryData
import zoo.animals.screens.CategoryAnimalsScreen
import zoo.animals.ui.theme.ZooTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            ZooTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimalData.init(LocalContext.current)
                    Navigation()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun TopBar(
    title: String,
    navController: NavController,
    showSearch: Boolean = true,
    showBackBtn: Boolean = false,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    var searching by rememberSaveable { mutableStateOf(false) }
    var searchedAnimals by remember { mutableStateOf(mutableMapOf<String, Animal>()) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { ContentAnimation().ShrinkInFromHorizontallySide(-200, 100, !searching) { Text(title) } },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else{
                                drawerState.open()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
//                    this.apply {
//                        scope.launch {
//                            if (drawerState.isOpen) {
//                                drawerState.close()
//                            }
//                        }
//                    }

                    searchingButton(searching, showSearch, {searching = it}, {searchedAnimals = it})

                    if (showBackBtn){
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    scrimColor = MaterialTheme.colorScheme.background,
                    drawerContent = {
                        DrawerView(navController = navController)
                    },
                    content = {
                        if (searchedAnimals.isEmpty()){
                            content()
                        } else{
                            CategoryAnimalsScreen().Screen(
                                navController = navController,
                                animals = searchedAnimals,
                                title = "",
                                showTopBar = false
                            )
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun DrawerView(navController: NavController) {
    val categoryIcon = remember { Icons.Filled.Category }
    val cameraIcon = remember { Icons.Filled.CameraAlt }

    val categoryTitleText = remember { UiTexts.StringResource(R.string.categoryTitle) }
    val cameraTileText = remember { UiTexts.StringResource(R.string.detectionByCamera) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            DrawerButton(categoryIcon, categoryTitleText.asString(), navController, Routes.Categories.route, true)
        }
        item {
            DrawerButton(cameraIcon, cameraTileText.asString(), navController, Routes.Camera.route, false)
        }
        item {
            Button(
                onClick = {  },
                Modifier
                    .width(300.dp)
                    .padding(top = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Row(modifier = Modifier
                    .width(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

                    Text(
                        text = "Seznam Zoo",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchingButton(
    searching: Boolean,
    showSearch: Boolean,
    _searching: (Boolean) -> Unit,
    _searchedAnimal: (MutableMap<String, Animal>) -> Unit): Boolean
{
    var searchingText by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    if (searching){
        ContentAnimation().ShrinkInFromHorizontallySide(0, 500, searching) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            TextField(
                value = searchingText,
                onValueChange = { searchingText = it },

                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                maxLines = 1,

                modifier = Modifier
                    .focusRequester(focusRequester)
                    .clip(RoundedCornerShape(10.dp)),
//                keyboardActions = KeyboardActions(onDone = { searching = false })
            )
        }
    }

    if (showSearch){
        Crossfade(
            targetState = !searching,
            animationSpec = TweenSpec(durationMillis = 350)
        ) { isChecked ->
            if (isChecked) {
                IconButton(onClick = { _searching(true) }) {
                    Icon(Icons.Filled.Search, contentDescription = null)
                }
            } else {
                IconButton(onClick = {
                    _searching(false)
                    searchingText = ""
                }) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                }
            }
        }
    }

    var searchedAnimals = remember { mutableMapOf<String, Animal>() }
    if (searchingText.length >= 2){
        searchedAnimals = (
                AnimalData.allAnimalsInstance[0] +
                AnimalData.allAnimalsInstance[1] +
                AnimalData.allAnimalsInstance[2]
                ) as MutableMap<String, Animal>


        searchedAnimals =
            searchedAnimals.filter { it.key
                    .replace("\\s".toRegex(), "")
                    .lowercase().normalize()

                    .contains(searchingText.replace(
                        "\\s".toRegex(),
                        ""
                    ).lowercase().normalize())
            } as MutableMap<String, Animal>
    }

    _searchedAnimal(searchedAnimals)
    return searchingText.length >= 2
}


@Composable
fun DrawerButton(
    icon: ImageVector,
    title: String,
    navController: NavController,
    route: String,
    expand: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Box{
        Button(
            onClick = { navController.navigate(route) },
            Modifier
                .width(300.dp)
                .padding(top = 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Row(modifier = Modifier
                .width(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(40.dp)
                )

                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

                Text(
                    text = title,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                )


                if (expand) {
                    IconButton(
                        onClick = { expanded = !expanded },
                    ) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = title,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
            }
        }

        Box(
            Modifier
                .padding(top = 50.dp)
                .align(Alignment.BottomEnd)
        ){
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                CategoryData.categoriesList(LocalContext.current).forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            navController.navigate(item.route.route)
                            expanded = false
                        },
                        text = {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                            {
                                Text(
                                    text = item.categoryName,
//                                    color = MaterialTheme.colorScheme.background,
                                    textAlign = TextAlign.Center
                                )
                            } }
                    )
                }
            }
        }
    }
}
