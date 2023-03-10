package zoo.animals.feature_category.view


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zoo.animals.LineTextDivider
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_category.data.Animal
import zoo.animals.feature_category.data.AnimalData
import zoo.animals.feature_category.data.ZooAnimalViewModel
import zoo.animals.shared.BottomContentSwitch
import zoo.animals.shared.TopBar
import zoo.animals.stringMapToIndexKey
import java.util.*
import kotlin.math.absoluteValue


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowAnimalInfo(
    navController: NavController,
    animalData: Animal,
    showTopBar: Boolean,
    viewModel: ZooAnimalViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    var animalKey: String? = ""
    when(animalData.category){
        UiTexts.ArrayResource(R.array.animalCategories, 0).asString() -> {
            animalKey = AnimalData.allAnimalsInstance[0].filterValues { it == animalData }.keys.firstOrNull()
        }
        UiTexts.ArrayResource(R.array.animalCategories, 1).asString() -> {
            animalKey = AnimalData.allAnimalsInstance[1].filterValues { it == animalData }.keys.firstOrNull()
        }
        UiTexts.ArrayResource(R.array.animalCategories, 2).asString() -> {
            animalKey = AnimalData.allAnimalsInstance[2].filterValues { it == animalData }.keys.firstOrNull()
        }
    }
    val context = LocalContext.current

    val permissions = rememberMultiplePermissionsState(permissions = listOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ))
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissions.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    if (permissions.allPermissionsGranted) {
        viewModel.startLocationUpdates(context)
    }

    LaunchedEffect(Unit, viewModel.state.closestZooKey.value){
        animalKey?.let { viewModel.getAnimalsInZoo(context, it) }
    }
    val validZooKeys by viewModel.validZooKeys.collectAsState()

    LaunchedEffect(key1 = viewModel.state.isGpsEnabled.value){
        if (viewModel.state.isGpsEnabled.value){
            viewModel.startLocationUpdates(context)
        } else{
            viewModel.state.closestZooKey.value = null
        }
    }

    SnackbarContent(animalData, animalKey){
        Box(modifier = Modifier.fillMaxSize()){
            if (showTopBar){
                TopBar(
                    title = animalData.name,
                    navController = navController,
                    showSearch = false,
                    showBackBtn = true,
                ) {
                    if (validZooKeys.isEmpty()){
                        MainContentScreen(animalData)
                    } else{
                        ContentAnimation().ScaleIn(duration = 450) {
                            BottomContentSwitch(
                                mainText = "Informace",
                                secondText = "V zoo",
//                            thirdText = "Fotogalerie",
                                thirdText = null,
                                mainPreview = { MainContentScreen(animalData) },
                                secondPreview = { animalKey?.let { SecondContentScreen(it, viewModel) } },
                                thirdPreview = null
                            )
                        }
                    }
                }
            } else{
                MainContentScreen(animalData)
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun GpsStatusChecker(update: (Boolean) -> Unit) {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var gpsEnabled by remember { mutableStateOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) }
    val gpsSettingsObserver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
    }
    DisposableEffect(key1 = context) {
        val intentFilter = IntentFilter(LocationManager.MODE_CHANGED_ACTION)
        context.registerReceiver(gpsSettingsObserver, intentFilter)

        onDispose {
            context.unregisterReceiver(gpsSettingsObserver)
        }
    }
    update(gpsEnabled)
}


@Composable
fun SnackbarContent(animal: Animal, animalKey: String?, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val snackBarTexts = remember {
        UiTexts.ArrayResource(R.array.snackBar,0).asArray(context)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    action = {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    animalKey?.let { AnimalData.saveSeenAnimal(it, context) }
                                }
                                data.performAction()
                            },
                        ) { Text(
                            snackBarTexts[2],
                            fontWeight = FontWeight.Bold
                        ) }
                    },
                    dismissAction = {
                        TextButton(
                            onClick = { data.dismiss() },
                        ) { Text(
                            snackBarTexts[3],
                            fontStyle = FontStyle.Italic,
                        ) }
                    }
                ) {
                    Text(snackBarTexts[1])
                }
            }
        },
        floatingActionButton = {
            if (!animal.seen){
                LaunchedEffect(Unit){
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "",
                            withDismissAction = true,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                }
            }
        },
    ){
        Box(Modifier.padding(it)){
            content()
        }
    }
}


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SecondContentScreen(
    animalKey: String,
    viewModel: ZooAnimalViewModel
){
    val context = LocalContext.current
    var expanded by rememberSaveable { mutableStateOf(false) }

    GpsStatusChecker { viewModel.state.isGpsEnabled.value = it }

    val informations by viewModel.informations.collectAsState()
    val validZooKeys by viewModel.validZooKeys.collectAsState()


    val images = rememberSaveable(informations) { mutableListOf(
        viewModel.state.image1.value,
        viewModel.state.image2.value,
        viewModel.state.image3.value
    ).filterNotNull() }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(10f)
        ) {
            stickyHeader {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                ){
                    val gpsStatus = remember(viewModel.state.isGpsEnabled.value) {
                        if (viewModel.state.isGpsEnabled.value) {
                            mutableStateOf(UiTexts.StringResource(R.string.gpsActive).asString(context))
                        } else {
                            mutableStateOf(
                                UiTexts.StringResource(R.string.gpsNotActive).asString(context)
                            )
                        }
                    }
                    Text(
                        "GPS: ${gpsStatus.value}", color = if (viewModel.state.isGpsEnabled.value) {
                            Color.Green
                        } else Color.Red
                    )
                    Text(UiTexts.StringResource(R.string.closestZoo).asString() + (viewModel.state.zoos[viewModel.state.closestZooKey.value ?: ""]?.city ?: "NONE"))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = viewModel.state.selectedZoo.value?.type + " " + viewModel.state.selectedZoo.value?.city,
                            onValueChange = {},
                            singleLine = true,
                            label = { Text(UiTexts.StringResource(R.string.chooseZoo).asString()) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            viewModel.state.zoos.forEach { selectionOption ->
                                if (selectionOption.key in validZooKeys){
                                    DropdownMenuItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = { Text(selectionOption.value.type + " " + selectionOption.value.city) },
                                        onClick = {
                                            viewModel.state.selectedZooKey.value = selectionOption.key
                                            viewModel.state.selectedZoo.value = selectionOption.value
                                            viewModel.getAnimalsInZoo(context, animalKey)

                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                }
            }


            item {
                if (informations.isNotEmpty()){
                    informations.forEach {info ->
                        val info1 = info?.split(":")
                        info1?.let { it ->
                            val title = it[0] + ":"
                            val values = it[1].split(";")

                            Text(
                                text = title,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            )
                            Column {
                                repeat(values.size){ i ->
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 8.dp)
                                            .fillMaxWidth()
                                    ){
                                        Icon(
                                            Icons.Filled.FiberManualRecord, contentDescription = "Dot",
                                            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                                        )
                                        Text(
                                            text = values[i].replaceFirst(" ", ""),
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        val pagerState = rememberPagerState()
        var clicked by rememberSaveable { mutableStateOf(false) }
        val imagesWeight = animateFloatAsState(if (clicked) 5000f else 4.5f)

        if (images.isNotEmpty()){
            Column(
                modifier = Modifier
                    .weight(imagesWeight.value)
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            clicked = !clicked
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    count = images.size,
                    modifier = Modifier.weight(1f),
                    state = pagerState
                ) { page ->
                    Card(
                        Modifier
                            .graphicsLayer {
                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                                lerp(
                                    start = 0.7f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(images[page]),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        )
                    }
                }

                HorizontalPagerIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 4.dp),
                    pagerState = pagerState,
                    activeColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorHeight = 4.dp
                )
            }
        }
    }
}


@Composable
fun MainContentScreen(animalData: Animal){
    val infoKeys = remember { stringMapToIndexKey(animalData.info) }
    val animations = ContentAnimation()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box{
                Image(
                    painter = painterResource(id = animalData.mainImage),
                    contentDescription = animalData.name,
                    modifier = Modifier
                        .height(450.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillHeight,
                )

                Box(modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 700f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
                )

                Box(modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 90.dp, start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        Modifier.fillMaxSize()
                    ) {
                        Box(Modifier.weight(1f)){
                            animations.FadeInFromVerticallySide(offsetY = -500, duration = 800) {
                                Text(
                                    animalData.name,
                                    fontSize = 50.sp,
                                    lineHeight = 50.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                )
                            }
                        }

                        if (animalData.canDetect){
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                Modifier
                                    .weight(0.15f)
                                    .align(Alignment.Bottom)
                            ){
                                animations.ScaleIn(800) {
                                    Image(
                                        painter = painterResource(R.drawable.icon_possible_to_see),
                                        contentDescription = "",
                                    )
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ){
                    val animalSize: List<String?> =
                        if (animalData.info[infoKeys[1]] == "None"){
                            listOf(infoKeys[2], infoKeys[3])
                        } else if (animalData.info[infoKeys[2]] == "None"){
                            listOf(infoKeys[1], infoKeys[3])
                        } else{
                            listOf(infoKeys[1], infoKeys[2])
                        }
                    val weight = infoKeys[0]

                    animations.FadeInFromHorizontallySide(offsetX = -500, duration = 800) {
                        RowDetailsContent(
                            animalData = animalData,
                            listOf(
                                animalSize[0]!!,
                                animalSize[1]!!,
                                weight!!
                            ))
                    }
                }
            }
        }

        item{
            Divider(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 16.dp)
                    .offset(y = (-10).dp)
            )

            animations.FadeInFromHorizontallySide(offsetX = 500, duration = 800) {
                RowDetailsContent(
                    animalData = animalData,
                    listOf(
                        infoKeys[4]!!,
                        infoKeys[6]!!
                    ))
            }
        }

        item{
            var textMaxLines by rememberSaveable { mutableStateOf(6) }
            Text(
                text = animalData.description.replaceFirst(" ", ""),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = 0.8f,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .clickable(
                        onClick = {
                            textMaxLines = if (textMaxLines == Int.MAX_VALUE) 6 else Int.MAX_VALUE
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                maxLines = textMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }

        item{
            val text = remember {
                buildAnnotatedString {
                    val text = UiTexts.StringResource(R.string.searchOnTheInternet).asString(context)
                    append(text)

                    addStyle(
                        style = SpanStyle(
                            color = Color(0xff64B5F6),
                            fontSize = 18.sp,
                            textDecoration = TextDecoration.Underline,
                        ), start = 0, end = text.length
                    )
                    addStringAnnotation(
                        tag = "URL",
                        annotation = "https://www.google.com/search?q=${animalData.name}",
                        start = 0, end = text.length
                    )
                }
            }
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = text,
                    maxLines = 1,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                text
                                    .getStringAnnotations("URL", 0, text.length)
                                    .firstOrNull()
                                    ?.let { stringAnnotation ->
                                        uriHandler.openUri(stringAnnotation.item)
                                    }
                            })
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        val taxonomies = animalData.taxonomyMain + animalData.taxonomyOrder + animalData.taxonomyFamily + animalData.taxonomyGenus
        val taxonomyKeys = stringMapToIndexKey(taxonomies)
        item{
            var starter by rememberSaveable { mutableStateOf(false) }
            val sizeWidth: Float by animateFloatAsState(
                targetValue = if (starter) 1f else 0f,
                animationSpec = tween(800)
            )
            val scope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit){
                scope.launch {
//                delay(200)
                    starter = true
                }
            }


            Divider(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = UiTexts.StringResource(R.string.taxonomy).asString(),
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            for (i in 0 until taxonomyKeys.size){
                taxonomyKeys[i]?.let {
                    LineTextDivider(
                        text = it,
                        textAlignment = Alignment.CenterStart,
                        offset = Offset(20f, 0f)
                    )
                }

                if (taxonomies[taxonomyKeys[i]] is List<*>){
                    for (text in (taxonomies[taxonomyKeys[i]] as List<*>)){
                        if (text != ""){
                            Text(
                                text = text.toString(),
                                fontSize = 21.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .fillMaxHeight()
                                    .fillMaxWidth(sizeWidth)
                            )
                        }
                    }
                } else{
                    Text(
                        text = taxonomies[taxonomyKeys[i]].toString(),
                        fontSize = 21.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxHeight()
                            .fillMaxWidth(sizeWidth)
                    )
                }
            }
        }

        item {
            Box(Modifier.padding(top = 16.dp))
            Divider(
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(horizontal = 16.dp)
            )

            Text(
                text = UiTexts.StringResource(R.string.appearance).asString(),
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ){
                Icon(
                    Icons.Filled.FiberManualRecord, contentDescription = "Dot",
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                animalData.info[infoKeys[5]]?.let { Text(it) }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CanvasMap(animalData.appearance)
            }
        }

        item {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)) {
            }
        }
    }
}


@Composable
fun CanvasMap(coords: List<List<Float>>){
    val painter = painterResource(R.drawable.world_map_clipped)
    var starter by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val originalX by remember { mutableStateOf(2685f) }
    val originalY by remember { mutableStateOf(1565f) }

    val radiusAnimation: Float by animateFloatAsState(
        targetValue = if (starter) 40f else 30f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(key1 = starter){
        scope.launch {
            delay(800)
            starter = !starter
        }
    }

    Image(
        modifier = Modifier
            .aspectRatio(originalX / originalY)
            .fillMaxWidth(),
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Fit
    )

    var sizes by remember { mutableStateOf(IntSize.Zero) }
    var scaleX by remember { mutableStateOf(originalX / sizes.width) }
    var scaleY by remember { mutableStateOf(originalY / sizes.height) }
    LaunchedEffect(key1 = sizes){
        scaleX = originalX / sizes.width
        scaleY = originalY / sizes.height
    }

    Canvas(modifier = Modifier
        .aspectRatio(originalX / originalY)
        .fillMaxWidth()
        .onGloballyPositioned { sizes = it.size }
    ){
        coords.forEach { xy ->
            drawCircle(Color.Red.copy(alpha = 0.5f), radius = (radiusAnimation * xy.getOrElse(2){1}.toFloat()), center = Offset(xy[0] / scaleX, xy[1] / scaleY))
        }
    }
}



@Composable
fun RowDetailsContent(animalData: Animal, infoKey: List<String>){
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        for (index in 0..infoKey.lastIndex){
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box{
                    Box(modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp)
                        .align(Alignment.TopCenter)
                    ) {
                        Text(infoKey[index],
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(top = 25.dp, start = 6.dp, end = 6.dp)
                            .align(Alignment.Center)
                    ) {
                        animalData.info[infoKey[index]]?.let {
                            Text(
                                it,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }

            if (index+1 <= infoKey.lastIndex){
                Divider(
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .width(1.dp)
                )
            }
        }
    }
}


@Composable
fun CameraSheetInfo(animalData: Animal?){
    animalData?.let { animal ->
        Box(modifier = Modifier.fillMaxHeight()){
            LazyColumn(modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .padding(bottom = 0.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                item {
                    Box(modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = animal.previewImage),
                            contentDescription = animal.name,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(200.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillHeight
                        )

                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .border(2.dp, Color.White, shape = RoundedCornerShape(35.dp))
                            .fillMaxWidth(0.7f)
                    ){
                        Text(
                            text = animal.name,
                            textAlign = TextAlign.Center,
                            fontSize = 50.sp,
                            lineHeight = 50.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}