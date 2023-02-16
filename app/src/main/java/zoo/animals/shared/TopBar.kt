package zoo.animals.shared

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_category.view.CategoryAnimalsScreen
import zoo.animals.navigation.Routes
import zoo.animals.normalize


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun TopBar(
    title: String,
    navController: NavController,
    showSearch: Boolean = true,
    showBackBtn: Boolean = false,
    gestureEnabled: Boolean = true,
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
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    ContentAnimation().ShrinkInFromHorizontallySide(-200, 100, !searching) {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
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
                actions ={
                    this.also {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            }
                        }
                    }

                    searchingButton(searching, showSearch, {searching = it}, {searchedAnimals = it})

                    if (showBackBtn){
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                },
            )
        },
        content = { innerPadding ->

            ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = gestureEnabled,
                drawerContent = {
                    Box(modifier = Modifier.padding(innerPadding)){
                        DrawerView(navController = navController)
                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                }
            )
        }
    )
}


@Composable
fun DrawerView(navController: NavController) {
    val categoryIcon = remember { Icons.Filled.Category }
    val cameraIcon = remember { Icons.Filled.CameraAlt }
    val discoveriesIcon = remember { Icons.Filled.TravelExplore }
    val aboutIcon = remember { Icons.Filled.Info }
    val settingsIcon = remember { Icons.Filled.Settings }

    val categoryTitleText = remember { UiTexts.StringResource(R.string.categoryTitle) }
    val cameraTileText = remember { UiTexts.StringResource(R.string.detectionByCamera) }
    val discoveriesTitleText = remember { UiTexts.StringResource(R.string.discoveries) }
    val appAboutTitleText = remember { UiTexts.StringResource(R.string.aboutApp) }
    val settingsTitleText = remember { UiTexts.StringResource(R.string.settings) }

    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(categoryIcon, contentDescription = null) },
            label = { Text(categoryTitleText.asString(),
                fontSize = if (navController.currentDestination?.route.equals(Routes.Categories.route)) 22.sp else 18.sp) },
            selected = navController.currentDestination?.route.equals(Routes.Categories.route) ||
                        navController.currentDestination?.route.equals(Routes.Mammals.route) ||
                        navController.currentDestination?.route.equals(Routes.Birds.route) ||
                        navController.currentDestination?.route.equals(Routes.Reptiles.route) ||
                        navController.currentDestination?.route.equals(Routes.AnimalInfo.route),
            onClick = {
                scope.launch {
                    navController.navigate(Routes.Categories.route)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            icon = { Icon(cameraIcon, contentDescription = null) },
            label = { Text(cameraTileText.asString(),
                fontSize = if (navController.currentDestination?.route.equals(Routes.Camera.route)) 22.sp else 18.sp) },
            selected = navController.currentDestination?.route.equals(Routes.Camera.route),
            onClick = {
                scope.launch {
                    navController.navigate(Routes.Camera.route)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            icon = { Icon(discoveriesIcon, contentDescription = null) },
            label = { Text(discoveriesTitleText.asString(),
                    fontSize = if (navController.currentDestination?.route.equals(Routes.Discovers.route)) 22.sp else 18.sp) },
            selected = navController.currentDestination?.route.equals(Routes.Discovers.route) ||
                        navController.currentDestination?.route.equals(Routes.AnimalsDiscovery.route) ||
                        navController.currentDestination?.route.equals(Routes.Zoos.route) ||
                        navController.currentDestination?.route.equals(Routes.ZooInfo.route),
            onClick = {
                scope.launch {
                    navController.navigate(Routes.Discovers.route)
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxSize()
        ){
            NavigationDrawerItem(
                icon = { Icon(settingsIcon, contentDescription = null) },
                label = { Text(settingsTitleText.asString(),
                    fontSize = if (navController.currentDestination?.route.equals(Routes.Settings.route)) 22.sp else 18.sp) },
                selected = navController.currentDestination?.route.equals(Routes.Settings.route),
                onClick = {
                    scope.launch {
                        navController.navigate(Routes.Settings.route)
                    }
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
            )

            NavigationDrawerItem(
                icon = { Icon(aboutIcon, contentDescription = null) },
                label = { Text(appAboutTitleText.asString(),
                    fontSize = if (navController.currentDestination?.route.equals(Routes.About.route)) 22.sp else 18.sp) },
                selected = navController.currentDestination?.route.equals(Routes.About.route),
                onClick = {
                    scope.launch {
                        navController.navigate(Routes.About.route)
                    }
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
            )
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
            searchedAnimals.filter { it.value.name
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

