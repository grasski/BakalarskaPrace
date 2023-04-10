@file:Suppress("NAME_SHADOWING")

package zoo.animals.feature_discovery.zoos
/**
 * Copyright 2022 Saket Narayan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.saket.swipe.*
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.clipRect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

/**
 * A composable that can be swiped left or right for revealing actions.
 *
 * @param swipeThreshold Minimum drag distance before any [SwipeAction] is
 * activated and can be swiped.
 *
 * @param backgroundUntilSwipeThreshold Color drawn behind the content until
 * [swipeThreshold] is reached. When the threshold is passed, this color is
 * replaced by the currently visible [SwipeAction]'s background.
 */
@Composable
fun SwipeableActionsBox(
    modifier: Modifier = Modifier,
    state: SwipeableActionsState = rememberSwipeableActionsState(),
    startActions: List<SwipeAction> = emptyList(),
    endActions: List<SwipeAction> = emptyList(),
    swipeThreshold: Dp = 40.dp,
    backgroundUntilSwipeThreshold: Color = Color.DarkGray,
    content: @Composable BoxScope.() -> Unit
) = BoxWithConstraints(modifier) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val leftActions = if (isRtl) endActions else startActions
    val rightActions = if (isRtl) startActions else endActions
    val swipeThresholdPx = LocalDensity.current.run { swipeThreshold.toPx() }

    val ripple = remember {
        SwipeRippleState()
    }
    val actions = remember(leftActions, rightActions) {
        ActionFinder(left = leftActions, right = rightActions)
    }
    LaunchedEffect(state, actions) {
        state.run {
            canSwipeTowardsRight = { leftActions.isNotEmpty() }
            canSwipeTowardsLeft = { rightActions.isNotEmpty() }
        }
    }

    val offset = state.offset.value
    val thresholdCrossed = abs(offset) > swipeThresholdPx

    var swipedAction: SwipeActionMeta? by remember {
        mutableStateOf(null)
    }
    val visibleAction: SwipeActionMeta? = remember(offset, actions) {
        actions.actionAt(offset, totalWidth = constraints.maxWidth)
    }
    val backgroundColor: Color by animateColorAsState(
        when {
            swipedAction != null -> swipedAction!!.value.background
            !thresholdCrossed -> backgroundUntilSwipeThreshold
            visibleAction == null -> Color.Transparent
            else -> visibleAction.value.background
        }
    )

    Box(
        modifier = Modifier
            .background(backgroundColor)       // Added only this
            .absoluteOffset { IntOffset(x = offset.roundToInt(), y = 0) }
            .drawOverContent { ripple.draw(scope = this) }
            .draggable(
                orientation = Horizontal,
                enabled = !state.isResettingOnRelease,
                onDragStopped = {
                    if (thresholdCrossed && visibleAction != null) {
                        swipedAction = visibleAction
                        swipedAction!!.value.onSwipe()
                        ripple.animate(
                            action = swipedAction!!,
                            scope = this
                        )
                    }
                    launch {
                        state.resetOffset()
                        swipedAction = null
                    }
                },
                state = state.draggableState,
            ),
        content = content
    )

    (swipedAction ?: visibleAction)?.let { action ->
        ActionIconBox(
            modifier = Modifier.matchParentSize(),
            action = action,
            offset = offset,
            backgroundColor = backgroundColor,
            content = { action.value.icon() }
        )
    }
}

@Composable
private fun ActionIconBox(
    action: SwipeActionMeta,
    offset: Float,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(width = placeable.width, height = placeable.height) {
                    // Align icon with the left/right edge of the content being swiped.
                    val iconOffset = if (action.isOnRightSide) constraints.maxWidth + offset else offset - placeable.width
                    placeable.placeRelative(x = iconOffset.roundToInt(), y = 0)
                }
            }
            .background(color = backgroundColor),
        horizontalArrangement = if (action.isOnRightSide) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

private fun Modifier.drawOverContent(onDraw: DrawScope.() -> Unit): Modifier {
    return drawWithContent {
        drawContent()
        onDraw(this)
    }
}



@Stable
internal class SwipeRippleState {
    private var ripple = mutableStateOf<SwipeRipple?>(null)

    fun animate(
        action: SwipeActionMeta,
        scope: CoroutineScope
    ) {
        val drawOnRightSide = action.isOnRightSide
        val action = action.value

        ripple.value = SwipeRipple(
            isUndo = action.isUndo,
            rightSide = drawOnRightSide,
            color = action.background,
            alpha = 0f,
            progress = 0f
        )

        // Reverse animation feels faster (especially for larger swipe distances) so slow it down further.
        val animationDurationMs = (animationDurationMs * (if (action.isUndo) 1.75f else 1f)).roundToInt()

        val progressAsync = scope.async {
            Animatable(initialValue = 0f).animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDurationMs),
                block = {
                    ripple.value = ripple.value!!.copy(progress = value)
                }
            )
        }
        val alphaAsync = scope.async {
            Animatable(initialValue = if (action.isUndo) 0f else 0.25f).animateTo(
                targetValue = if (action.isUndo) 0.5f else 0f,
                animationSpec = tween(
                    durationMillis = animationDurationMs,
                    delayMillis = if (action.isUndo) 0 else animationDurationMs / 2
                ),
                block = {
                    ripple.value = ripple.value!!.copy(alpha = value)
                }
            )
        }

        scope.launch {
            progressAsync.await()
            alphaAsync.await()
        }
    }

    fun draw(scope: DrawScope) {
        ripple.value?.run {
            scope.clipRect {
                val size = scope.size
                // Start the ripple with a radius equal to the available height so that it covers the entire edge.
                val startRadius = if (isUndo) size.width + size.height else size.height
                val endRadius = if (!isUndo) size.width + size.height else size.height
                val radius = lerp(startRadius, endRadius, fraction = progress)

                drawCircle(
                    color = color,
                    radius = radius,
                    alpha = alpha,
                    center = this.center.copy(x = if (rightSide) this.size.width + this.size.height else 0f - this.size.height)
                )
            }
        }
    }
}

private data class SwipeRipple(
    val isUndo: Boolean,
    val rightSide: Boolean,
    val color: Color,
    val alpha: Float,
    val progress: Float,
)

private fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)


internal data class SwipeActionMeta(
    val value: SwipeAction,
    val isOnRightSide: Boolean,
)

internal data class ActionFinder(
    private val left: List<SwipeAction>,
    private val right: List<SwipeAction>
) {

    fun actionAt(offset: Float, totalWidth: Int): SwipeActionMeta? {
        if (offset == 0f) {
            return null
        }

        val isOnRightSide = offset < 0f
        val actions = if (isOnRightSide) right else left

        val actionAtOffset = actions.actionAt(
            offset = abs(offset).coerceAtMost(totalWidth.toFloat()),
            totalWidth = totalWidth
        )
        return actionAtOffset?.let {
            SwipeActionMeta(
                value = actionAtOffset,
                isOnRightSide = isOnRightSide
            )
        }
    }

    private fun List<SwipeAction>.actionAt(offset: Float, totalWidth: Int): SwipeAction? {
        if (isEmpty()) {
            return null
        }

        val totalWeights = this.sumOf { it.weight }
        var offsetSoFar = 0.0

        @Suppress("ReplaceManualRangeWithIndicesCalls") // Avoid allocating an Iterator for every pixel swiped.
        for (i in 0 until size) {
            val action = this[i]
            val actionWidth = (action.weight / totalWeights) * totalWidth
            val actionEndX = offsetSoFar + actionWidth

            if (offset <= actionEndX) {
                return action
            }
            offsetSoFar += actionEndX
        }

        // Precision error in the above loop maybe?
        error("Couldn't find any swipe action. Width=$totalWidth, offset=$offset, actions=$this")
    }
}


@Composable
fun rememberSwipeableActionsState(): SwipeableActionsState {
    return remember { SwipeableActionsState() }
}

/**
 * The state of a [SwipeableActionsBox].
 */
@Stable
class SwipeableActionsState internal constructor() {
    /**
     * The current position (in pixels) of a [SwipeableActionsBox].
     */
    val offset: State<Float> get() = offsetState
    internal var offsetState = mutableStateOf(0f)

    /**
     * Whether [SwipeableActionsBox] is currently animating to reset its offset after it was swiped.
     */
    var isResettingOnRelease: Boolean by mutableStateOf(false)
        private set

    internal lateinit var canSwipeTowardsRight: () -> Boolean
    internal lateinit var canSwipeTowardsLeft: () -> Boolean

    internal val draggableState = DraggableState { delta ->
        val targetOffset = offsetState.value + delta
        val isAllowed = isResettingOnRelease
                || targetOffset > 0f && canSwipeTowardsRight()
                || targetOffset < 0f && canSwipeTowardsLeft()

        // Add some resistance if needed.
        offsetState.value += if (isAllowed) delta else delta / 10
    }

    internal suspend fun resetOffset() {
        draggableState.drag(MutatePriority.PreventUserInput) {
            isResettingOnRelease = true
            try {
                Animatable(offsetState.value).animateTo(targetValue = 0f, tween(durationMillis = animationDurationMs)) {
                    dragBy(value - offsetState.value)
                }
            } finally {
                isResettingOnRelease = false
            }
        }
    }
}


internal const val animationDurationMs = 4_00