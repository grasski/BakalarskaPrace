package zoo.animals.shared


import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zoo.animals.animations.ContentAnimation


@Composable
fun BottomContentSwitch(
    mainText: String,
    secondText: String,
    thirdText: String?,
    mainPreview: @Composable () -> Unit,
    secondPreview: @Composable () -> Unit,
    thirdPreview: (@Composable () -> Unit)?
){
    var screenSelected by rememberSaveable { mutableStateOf(0) }

    Scaffold (
        bottomBar = {
            BottomAppBar(
                Modifier.heightIn(30.dp, 50.dp),
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
            ){
                ScreenContentSwitch(
                    mainText,
                    secondText,
                    thirdText
                ){screenSelected = it}
            }
        }
    ){
        Box(Modifier.padding(it)){
            Crossfade(
                targetState = screenSelected,
                animationSpec = TweenSpec(durationMillis = 500)
            ) { screenNumber ->
                when(screenNumber){
                    0 -> {
                        ContentAnimation().FadeInFromHorizontallySide(offsetX = -5000, duration = 250) {
                            mainPreview()
                        }
                    }

                    1 -> {
                        ContentAnimation().FadeInFromHorizontallySide(offsetX = 5000, duration = 250) {
                            secondPreview()
                        }
                    }

                    2 -> {
                        if (thirdPreview != null) {
                            thirdPreview()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ScreenContentSwitch(textMain: String, textSecond: String, textThird: String?, selected: (Int) -> Unit) {
    var screenSelected by rememberSaveable { mutableStateOf(0) }

    val mainWidth: Float by animateFloatAsState(if (screenSelected == 0) 1f else 0f)
    val secondWidth: Float by animateFloatAsState(if (screenSelected == 1) 1f else 0f)
    val thirdWidth: Float by animateFloatAsState(if (screenSelected == 2) 1f else 0f)
    val interactionSource = MutableInteractionSource()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(30.dp, 50.dp)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .align(Alignment.CenterVertically)
            .clickable(
                onClick = { screenSelected = 0 },
                interactionSource = interactionSource,
                indication = null
            )
            .drawBehind {
                val lineOffset = 50f
                val lineWidth = size.width - lineOffset * 2
                val linePivot = size.width / 2

                drawLine(
                    Color.Green,
                    start = Offset(
                        x = linePivot - (lineWidth / 2 * mainWidth),
                        y = size.height
                    ),
                    end = Offset(
                        x = linePivot + (lineWidth / 2 * mainWidth),
                        y = size.height
                    ),
                    strokeWidth = 3f
                )
            },
            contentAlignment = Alignment.Center
        ){
            Text(textMain)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .align(Alignment.CenterVertically)
            .clickable(
                onClick = { screenSelected = 1 },
                interactionSource = interactionSource,
                indication = null
            )
            .drawBehind {
                val lineOffset = 50f
                val lineWidth = size.width - lineOffset * 2
                val linePivot = size.width / 2

                drawLine(
                    Color.Green,
                    start = Offset(
                        x = linePivot - (lineWidth / 2 * secondWidth),
                        y = size.height
                    ),
                    end = Offset(
                        x = linePivot + (lineWidth / 2 * secondWidth),
                        y = size.height
                    ),
                    strokeWidth = 3f
                )
            },
            contentAlignment = Alignment.Center
        ){
            Text(textSecond)
        }

        if (textThird != null){
            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .clickable(
                    onClick = { screenSelected = 2 },
                    interactionSource = interactionSource,
                    indication = null
                )
                .drawBehind {
                    val lineOffset = 50f
                    val lineWidth = size.width - lineOffset * 2
                    val linePivot = size.width / 2

                    drawLine(
                        Color.Green,
                        start = Offset(
                            x = linePivot - (lineWidth / 2 * thirdWidth),
                            y = size.height
                        ),
                        end = Offset(
                            x = linePivot + (lineWidth / 2 * thirdWidth),
                            y = size.height
                        ),
                        strokeWidth = 3f
                    )
                },
                contentAlignment = Alignment.Center
            ){
                Text(textThird)
            }
        }
    }

    selected(screenSelected)
}
