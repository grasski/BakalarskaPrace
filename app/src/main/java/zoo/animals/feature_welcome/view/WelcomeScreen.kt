package zoo.animals.feature_welcome.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation
import zoo.animals.feature_welcome.data.WelcomeScreenData
import zoo.animals.feature_welcome.data.WelcomeScreenViewModel
import zoo.animals.navigation.Routes


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(
    navController: NavHostController,
    viewModel: WelcomeScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pages = listOf(
        WelcomeScreenData.First(LocalContext.current),
        WelcomeScreenData.Second(LocalContext.current),
        WelcomeScreenData.Third(LocalContext.current)
    )
    val pagerState = rememberPagerState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(pages[position])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
        FinishButton(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize(),
            pagerState = pagerState
        ) {
            scope.launch {
                viewModel.saveWelcomeScreenState(context)
            }

            navController.popBackStack()
            navController.navigate(Routes.Categories.route)
        }
    }
}

@Composable
fun PagerScreen(screenData: WelcomeScreenData) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (screenData.title != ""){
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(screenData.image))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize()
            )
        } else{
            val initSize = 550f
            val transition = rememberInfiniteTransition()
            val imgSize by transition.animateFloat(
                initialValue = initSize/1.2f,
                targetValue = initSize,
                animationSpec = infiniteRepeatable(
                    animation = tween(1100, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Box(
                Modifier
                    .weight(0.5f)
                    .fillMaxSize()
            ){
                Image(
                    painter = painterResource(id = screenData.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(with(LocalDensity.current) { imgSize.toDp() })
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }
        }


        Text(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.2f),
            text = screenData.title,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.4f)
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = screenData.description,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit
) {
    Box(modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        if (pagerState.currentPage == 2){
            ContentAnimation().ScaleIn(
                duration = 400,
                pagerState.currentPage == 2
            ) {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(text = UiTexts.StringResource(R.string.finishButton).asString(), fontSize = 20.sp)
                }
            }
        }
    }
}
