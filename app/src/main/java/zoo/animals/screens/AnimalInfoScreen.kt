package zoo.animals.screens


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.data.Animal
import zoo.animals.TopBar
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
                animations.ScaleIn {
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


@Composable
fun ContentScreen(animalData: Animal){
    val infoKeys = remember { stringMapToIndexKey(animalData.info) }
    val animations = ContentAnimation()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                    .padding(bottom = 90.dp, start = 20.dp, end = 20.dp)
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
                        .padding(bottom = 20.dp)
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
                    .padding(start = 20.dp, end = 20.dp)
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
    }
}


@Composable
fun RowDetailsContent(animalData: Animal, infoKey: List<String>){
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
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