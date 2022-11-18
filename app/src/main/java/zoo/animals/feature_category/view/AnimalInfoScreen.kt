package zoo.animals.feature_category.view


import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_category.data.Animal
import zoo.animals.shared.TopBar
import zoo.animals.stringMapToIndexKey


@Composable
fun ShowAnimalInfo(navController: NavController, animalData: Animal?, showTopBar: Boolean){
    val animations = ContentAnimation()

    Box(modifier = Modifier.fillMaxSize()){
        if (showTopBar){
            TopBar(
                title = UiTexts.StringResource(R.string.empty).asString(),
                navController = navController,
                showSearch = false,
                showBackBtn = true,
            ) {
                animations.ScaleIn(duration = 450) {
                    if (animalData != null) {
                        ContentScreen(animalData)
                    }
                }
            }
        } else{
            if (animalData != null) {
                ContentScreen(animalData)
            }
        }
    }
}


@SuppressLint("RememberReturnType")
@Composable
fun ContentScreen(animalData: Animal){
    val infoKeys = remember { stringMapToIndexKey(animalData.info) }
    val animations = ContentAnimation()

    Column(){
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
                        animations.FadeInFromVerticallySide(offsetY = -500, duration = 800) {
                            Text(
                                animalData.name,
                                fontSize = 50.sp,
                                lineHeight = 50.sp,
                                fontWeight = FontWeight.ExtraBold,
                            )
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
                        .padding(start = 16.dp, end = 16.dp)
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
                Text(
                    text = animalData.description,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
            }

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CanvasMap(animalData.appearance)
                }
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
                    if (animalData != null) {
                        Image(
                            painter = painterResource(id = animalData.previewImage),
                            contentDescription = animalData.name,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(200.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .border(2.dp, Color.White, shape = RoundedCornerShape(35.dp))
                        .fillMaxWidth(0.7f)
                ){
                    if (animalData != null) {
                        Text(
                            text = animalData.name,
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