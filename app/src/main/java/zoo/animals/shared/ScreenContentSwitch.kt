package zoo.animals.shared

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
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
import zoo.animals.R
import zoo.animals.UiTexts
import zoo.animals.animations.ContentAnimation


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomContentSwitch(
    mainPreview: @Composable () -> Unit,
    secondPreview: @Composable () -> Unit
){
    var mainScreenSelected by rememberSaveable { mutableStateOf(true) }

    Scaffold (
        bottomBar = {
            BottomAppBar(
                Modifier.heightIn(30.dp, 50.dp),
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
            ){
                ScreenContentSwitch(
                    UiTexts.StringResource(R.string.liveCamera).asString(),
                    UiTexts.StringResource(R.string.photoSelect).asString()
                ){mainScreenSelected = it}
            }
        }
    ){
        Crossfade(
            targetState = mainScreenSelected,
            animationSpec = TweenSpec(durationMillis = 500)
        ) { isMain ->
            if (isMain) {
                ContentAnimation().FadeInFromHorizontallySide(offsetX = -5000, duration = 250) {
                    mainPreview()
                }
            } else {
                ContentAnimation().FadeInFromHorizontallySide(offsetX = 5000, duration = 250) {
                    secondPreview()
                }
            }
        }
    }
}


@Composable
fun ScreenContentSwitch(textMain: String, textSecond: String, selected: (Boolean) -> Unit) {
    var mainScreenSelected by rememberSaveable { mutableStateOf(true) }

    val mainWidth: Float by animateFloatAsState(if (mainScreenSelected) 1f else 0f)
    val secondWidth: Float by animateFloatAsState(if (!mainScreenSelected) 1f else 0f)
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
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
            .align(Alignment.CenterVertically)
            .clickable(
                onClick = { mainScreenSelected = true },
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
            .fillMaxSize()
            .align(Alignment.CenterVertically)
            .clickable(
                onClick = { mainScreenSelected = false },
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
    }

    selected(mainScreenSelected)
}
