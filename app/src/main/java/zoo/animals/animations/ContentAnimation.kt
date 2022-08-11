package zoo.animals.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@OptIn(ExperimentalAnimationApi::class)
class ContentAnimation {

    @Composable
    fun ScaleIn(content: @Composable () -> Unit){
        val visibleState = remember { MutableTransitionState(false) }
        visibleState.targetState = true

        AnimatedVisibility(
            visibleState = visibleState,
            modifier = Modifier,
            enter = scaleIn(animationSpec = tween(durationMillis = 450)) + expandVertically(expandFrom = Alignment.CenterVertically),
            exit = scaleOut(animationSpec = tween(durationMillis = 450)) + shrinkVertically(shrinkTowards = Alignment.CenterVertically),
        ){
            content()
        }
    }

    @Composable
    fun ShowsFromVerticallySide(offsetY: Int, duration: Int, targetState: Boolean=true, content: @Composable () -> Unit){
        val visibleState = remember { MutableTransitionState(false) }
        visibleState.targetState = targetState

        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = duration),
                initialOffsetY = { offsetY }
            ) + fadeIn(),
//            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
            exit = slideOutVertically(
                targetOffsetY = { offsetY },
                animationSpec = tween(durationMillis = duration)
            ) + fadeOut(),
        ){
            content()
        }
    }

    @Composable
    fun ShowsFromHorizontallySide(offsetX: Int, duration: Int, content: @Composable () -> Unit){
        val visibleState = remember { MutableTransitionState(false) }
        visibleState.targetState = true

        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = duration),
                initialOffsetX = { offsetX }
            ) + fadeIn(initialAlpha = 0.1f),
            exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut(),
        ){
            content()
        }
    }
}