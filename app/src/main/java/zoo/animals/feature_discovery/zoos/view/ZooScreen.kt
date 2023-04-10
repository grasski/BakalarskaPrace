package zoo.animals.feature_discovery.zoos.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_discovery.zoos.data.Zoo
import zoo.animals.feature_discovery.zoos.data.ZooData
import zoo.animals.shared.ScreenContentSwitch
import zoo.animals.shared.TopBar


@Composable
fun ZooScreen(navController: NavController, zoo: Zoo) {
    var infoSelected by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val switchTexts = remember {
        UiTexts.ArrayResource(R.array.switchTexts,0).asArray(context)
    }

    val zooKey = ZooData.allZoosInstance.filterValues { it == zoo }.keys.firstOrNull()
    TopBar(
        title = zoo.type,
        navController = navController,
        gestureEnabled = false,
        showSearch = false,
        showBackBtn = true,
        zooKey = zooKey
    ) {
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ContentAnimation().ScaleIn(
                    targetState = infoSelected == 0,
                    duration = 500)
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = zoo.city,
                            fontSize = 46.sp,
                            maxLines = 2,
                            lineHeight = 46.sp,
                            textAlign = TextAlign.Center
                        )
                        Divider(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                        ) }
                }

                ScreenContentSwitch(
                    switchTexts[0],
                    switchTexts[1],
                    null
                ) { infoSelected = it }

                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp)
                )
            }

            ContentAnimation().ScreensCrossFade(
                targetState = infoSelected == 0,
                duration = 500,
                trueScreen = { InfoScreen(zoo = zoo) },
                falseScreen = { MapScreen(zoo) }
            )
        }
    }
}
