package zoo.animals.feature_category.view


import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.feature_category.data.Category
import zoo.animals.shared.TopBar
import kotlin.random.Random
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Density
import zoo.animals.feature_category.data.CategoryData


@Composable
fun CategoryScreen(navController: NavController){
    val context = LocalContext.current


    TopBar(title = UiTexts.StringResource(R.string.categoryTitle).asString(), navController = navController)
    {
//        Box(Modifier.fillMaxSize()){
//            PawsBackground()

            LazyColumn {
                item {
                    CategoryCard(Arrangement.Start, CategoryData.mammals(context), navController)
                }
                item {
                    CategoryCard(Arrangement.End, CategoryData.birds(context), navController)
                }
                item {
                    CategoryCard(Arrangement.Start, CategoryData.reptiles(context), navController)
                }
            }
//        }
    }
}

fun randomPosition(configuration: Configuration, density: Density, positions: SnapshotStateMap<Int, SnapshotStateList<Any>>?): Offset {
    val screenWidth = with(density) {configuration.screenWidthDp.dp.roundToPx()}
    val screenHeight = with(density) {configuration.screenHeightDp.dp.roundToPx()}
    val random = Random(System.currentTimeMillis())

    var x = 0f
    var y = 0f

    if (positions != null){
        positions.forEach {
            val oldOffset = it.value[0] as Offset
            x = random.nextInt(0, screenWidth).toFloat()
            y = random.nextInt(0, screenHeight).toFloat()
            var r = 0

            while (((x-100 <= oldOffset.x) && (x+100 >= oldOffset.x)) && r < 2){
                x = random.nextInt(0, screenWidth).toFloat()
                r++
            }
            while (((y-100 <= oldOffset.y) && (y+100 >= oldOffset.y)) && r < 4){
                y = random.nextInt(0, screenHeight).toFloat()
                r++
            }

            if (r >= 4 && (((x-100 <= oldOffset.x) && (x+100 >= oldOffset.x)) || ((y-100 <= oldOffset.y) && (y+100 >= oldOffset.y)))){
                x = (screenWidth + 100).toFloat()
                y = (screenHeight + 100).toFloat()
            }

        }
    } else{
        x = random.nextInt(0, screenWidth).toFloat()
        y = random.nextInt(0, screenHeight).toFloat()
    }
    return Offset(x, y)
}


@Composable
fun PawsBackground() {
    val screenConfiguration = LocalConfiguration.current
    val density = LocalDensity.current

    val images = mutableListOf(
        ImageBitmap.imageResource(id = R.drawable.paw_cat),
        ImageBitmap.imageResource(id = R.drawable.paw_camel),
        ImageBitmap.imageResource(id = R.drawable.paw_cow),
        ImageBitmap.imageResource(id = R.drawable.paw_bear),
        ImageBitmap.imageResource(id = R.drawable.paw_dog),
        ImageBitmap.imageResource(id = R.drawable.paw_elephant),
        ImageBitmap.imageResource(id = R.drawable.paw_frog),
        ImageBitmap.imageResource(id = R.drawable.paw_goose),
        ImageBitmap.imageResource(id = R.drawable.paw_hedgehog),
        ImageBitmap.imageResource(id = R.drawable.paw_horse),
        ImageBitmap.imageResource(id = R.drawable.paw_alligator),
        ImageBitmap.imageResource(id = R.drawable.paw_fox),
        ImageBitmap.imageResource(id = R.drawable.paw_squirrel),
        ImageBitmap.imageResource(id = R.drawable.paw_pigeon),
    )

    val alphas = remember {
        mutableStateListOf(
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
        )
    }

    val positions = remember {
        mutableStateMapOf(
            0 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[0]),
            1 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[1]),
            2 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[2]),
            3 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[3]),
            4 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[4]),
            5 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[5]),
            6 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[6]),
            7 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[7]),
            8 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[8]),
            9 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[9]),
            10 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[10]),
            11 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[11]),
            12 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[12]),
            13 to mutableStateListOf(randomPosition(screenConfiguration, density, null), alphas[13]),
        )
    }

    repeat(14){
        LaunchedEffect(positions[it]?.get(0)){
            launch {
                val random = Random(System.currentTimeMillis())
                val time = random.nextInt(900, 1800).toInt()

                alphas[it].animateTo(
                    if (alphas[it].value == 1f) 0f else 1f,
                    animationSpec = tween(time)
                )
                delay(time.toLong())
                alphas[it].animateTo(
                    if (alphas[it].value == 1f) 0f else 1f,
                    animationSpec = tween(time)
                )
                delay(time.toLong())

                positions[it]?.set(0, randomPosition(screenConfiguration, density, positions))
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(14){
            positions[it]?.get(0)?.let { offset ->
                drawImage(images[it], offset as Offset, alpha = alphas[it].value)
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    position: Arrangement.Horizontal,
    category: Category,
    navController: NavController,
) {
    val image: Painter = painterResource(id = category.imageId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 15.dp)
            .heightIn(100.dp, 250.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = { navController.navigate(category.route.route) }
    ) {
        Row {
            if (position == Arrangement.End) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterVertically)
                ){
                    Text(
                        text = category.categoryName,
                        modifier = Modifier
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = image,
                        contentDescription = category.categoryName,
                        modifier = Modifier
                            .size(145.dp)
                            .align(Alignment.Center)
                            .padding(5.dp, 5.dp)
                    )
                }
            } else{
                Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Image(
                        painter = image,
                        contentDescription = category.categoryName,
                        modifier = Modifier
                            .size(145.dp)
                            .align(Alignment.Center)
                            .padding(5.dp, 5.dp)
                    )
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                ){
                    Text(
                        text = category.categoryName,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                    )
                }
            }
        }
    }
}
