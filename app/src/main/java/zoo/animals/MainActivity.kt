package zoo.animals


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.PopupMenu
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ButtonColors
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.data.CategoryData
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
                    Navigation()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text(title) },
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
                    if (showSearch){
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Filled.Search, contentDescription = null)
                        }
                    }
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
                    drawerContent = {
                        DrawerView(navController = navController)
                    },
                    content = {
                        content()
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
    }
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
                            }}
                    )
                }
            }
        }
    }
}



//    Button(
//        onClick = { navController.navigate(route) },
//        Modifier
//            .widthIn(100.dp, 300.dp)
//            .padding(top = 15.dp)
//            .height(100.dp)
//    ) {
//        Row(
//            Modifier
//                .heightIn(10.dp, 80.dp)
//                .fillMaxWidth()
//        ) {
//            Box(
//                Modifier
//                    .align(Alignment.CenterVertically)
//                    .fillMaxWidth(0.1f)
//            ){
//                Icon(
//                    imageVector = icon,
//                    contentDescription = title,
//                    modifier = Modifier
//                        .size(34.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.width(20.dp))
//
//            Box(
//                Modifier
//                    .align(Alignment.CenterVertically)
//                    .fillMaxWidth(0.85f),
//                contentAlignment = Alignment.CenterStart
//            ){
//                Text(
//                    text = title,
//                    fontSize = 22.sp,
//                    textAlign = TextAlign.Start,
//                    color = MaterialTheme.colorScheme.background
//                )
//            }
//
//            Box(
//                Modifier
//                    .align(Alignment.CenterVertically)
//                    .fillMaxWidth(),
//                contentAlignment = Alignment.CenterEnd
//            ){
//                if (expand) {
//                    IconButton(
//                        onClick = { expanded = !expanded },
//                    ) {
//                        Icon(
//                            Icons.Filled.ArrowDropDown,
//                            contentDescription = title,
//                            modifier = Modifier
//                                .size(40.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    Column {
//        AnimatedVisibility(
//            visible = expanded,
//
//            enter = slideInVertically(
//                initialOffsetY = { -40 }
//            ) + expandVertically(
//                expandFrom = Alignment.Top
//            ) + scaleIn(
//                transformOrigin = TransformOrigin(0.5f, 0f)
//            ) + fadeIn(initialAlpha = 0.2f),
//
//            exit = slideOutVertically(
//                targetOffsetY = { 0 }
//            ) + scaleOut(
//                transformOrigin = TransformOrigin(0.5f, 0f)
//            ) + shrinkVertically(
//                shrinkTowards = Alignment.Top
//            )
//        ) {
//            val context = LocalContext.current
//            Column {
//                CategoryData.categoriesList(context).forEach { item ->
//                    Button(onClick = { navController.navigate(item.route.route) }) {
//                        Text(
//                            text = item.categoryName,
//                            color = MaterialTheme.colorScheme.background
//                        )
//                    }
//                }
//            }
//        }
//    }
//        }
//}
