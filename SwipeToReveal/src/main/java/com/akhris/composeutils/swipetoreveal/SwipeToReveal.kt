package com.akhris.composeutils.swipetoreveal

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.swipetoreveal.RevealValue.*
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt


/**
 * The directions in which a [SwipeToReveal] can be dismissed.
 */
enum class RevealDirection {
    /**
     * Can be dismissed by swiping in the reading direction.
     */
    StartToEnd,

    /**
     * Can be dismissed by swiping in the reverse of the reading direction.
     */
    EndToStart
}


/**
 * Possible values of [RevealState].
 */
enum class RevealValue {
    /**
     * Indicates the component has not been revealed yet.
     */
    Default,

    /**
     * Indicates the component has been revealed in the reading direction.
     */
    RevealedToEnd,

    /**
     * Indicates the component has been revealed in the reverse of the reading direction.
     */
    RevealedToStart,

    /**
     * Indicates the component has been revealed completely and pushed a bit further to fire button's action
     */
    CommitedToEnd,

    /**
     * Indicates the component has been revealed completely and pushed a bit further to fire button's action
     */
    CommitedToStart
}


/**
 * State of the [SwipeToReveal] composable.
 *
 * @param initialValue The initial value of the state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@ExperimentalMaterialApi
class RevealState(
    initialValue: RevealValue,
    confirmStateChange: (RevealValue) -> Boolean = { true }
) : SwipeableState<RevealValue>(initialValue, confirmStateChange = confirmStateChange) {
    /**
     * The direction (if any) in which the composable has been or is being revealed.
     *
     * If the composable is settled at the default state, then this will be null. Use this to
     * change the background of the [SwipeToReveal] if you want different actions on each side.
     */
    val revealDirection: RevealDirection?
        get() = if (offset.value == 0f) null else if (offset.value > 0f) RevealDirection.StartToEnd else RevealDirection.EndToStart

    /**
     * Whether the component has been revealed in the given [direction].
     *
     * @param direction The reveal direction.
     */
    fun isRevealed(direction: RevealDirection): Boolean {
        return currentValue == if (direction == RevealDirection.StartToEnd) RevealedToEnd else RevealedToStart
    }

    /**
     * Reset the component to the default position with animation and suspend until it if fully
     * reset or animation has been cancelled. This method will throw [CancellationException] if
     * the animation is interrupted
     *
     * @return the reason the reset animation ended
     */
    suspend fun reset() = animateTo(targetValue = Default)

    /**
     * Reveal the component in the given [direction], with an animation and suspend. This method
     * will throw [CancellationException] if the animation is interrupted
     *
     * @param direction The reveal direction.
     */
    suspend fun reveal(direction: RevealDirection) {
        val targetValue =
            if (direction == RevealDirection.StartToEnd) RevealedToEnd else RevealedToStart
        animateTo(targetValue = targetValue)
    }

    companion object {
        /**
         * The default [Saver] implementation for [RevealState].
         */
        fun Saver(
            confirmStateChange: (RevealValue) -> Boolean
        ) = Saver<RevealState, RevealValue>(
            save = { it.currentValue },
            restore = { RevealState(it, confirmStateChange) }
        )
    }
}


/**
 * Create and [remember] a [RevealState].
 *
 * @param initialValue The initial value of the state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
@ExperimentalMaterialApi
fun rememberRevealState(
    initialValue: RevealValue = Default,
    confirmStateChange: (RevealValue) -> Boolean = { true }
): RevealState {
    return rememberSaveable(saver = RevealState.Saver(confirmStateChange)) {
        RevealState(initialValue, confirmStateChange)
    }
}

/**
 * A composable that can be dismissed by swiping left or right.
 *
 * @sample androidx.compose.material.samples.SwipeToDismissListItems
 *
 * @param state The state of this component.
 * @param modifier Optional [Modifier] for this component.
 * @param directions The set of directions in which the component can be dismissed.
 * @param revealThresholds The thresholds the item needs to be swiped in order to be dismissed.
 * @param background A composable that is stacked behind the content and is exposed when the
 * content is swiped. You can/should use the [state] to have different backgrounds on each side.
 * @param revealContent The content that can be dismissed.
 */
@Composable
@ExperimentalMaterialApi
fun SwipeToReveal(
    modifier: Modifier = Modifier,
    state: RevealState = rememberRevealState(),
    startButton: RevealButton? = null,
    endButton: RevealButton? = null,
    revealContent: @Composable BoxScope.() -> Unit
) = BoxWithConstraints(modifier) {

//    val state = rememberRevealState { value ->
//        when (value) {
//            RevealValue.RevealedToEnd -> {
//                buttons.find { it.direction == RevealDirection.StartToEnd }?.doOnReveal?.invoke()
//            }
//            RevealValue.RevealedToStart -> {
//                buttons.find { it.direction == RevealDirection.EndToStart }?.doOnReveal?.invoke()
//            }
//            else -> {
//            }
//        }
//        false
//    }


    val directions = remember(startButton, endButton) {
        val dirs = mutableListOf<RevealDirection>()
        startButton?.let { dirs.add(RevealDirection.StartToEnd) }
        endButton?.let { dirs.add(RevealDirection.EndToStart) }
        dirs.toList()
    }

    //width of the composable
    val width = remember { constraints.maxWidth.toFloat() }
    var height by remember { mutableStateOf(24f) }


    //max revealing width
    val revealWidthStartToEnd =
        remember(height, startButton) { startButton?.width ?: height }
    val revealWidthEndToStart =
        remember(height, endButton) { endButton?.width ?: height }

    Timber.d("revealWidthStartToEnd: $revealWidthStartToEnd")
    Timber.d("revealWidthEndToStart: $revealWidthEndToStart")

    val revealThresholds: (RevealDirection) -> ThresholdConfig = { FractionalThreshold(0.5f) }


    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val anchors = remember(height) {
        val map = mutableMapOf(0f to Default)
        if (RevealDirection.StartToEnd in directions) {
            map += revealWidthStartToEnd to RevealedToEnd
            map += revealWidthStartToEnd * 1.5f to CommitedToEnd
        }
        if (RevealDirection.EndToStart in directions) {
            map += -revealWidthEndToStart to RevealedToStart
            map += -revealWidthEndToStart * 1.5f to CommitedToStart
        }

        Timber.d("anchors: $map")
        map
    }

    val thresholds = { from: RevealValue, to: RevealValue ->
        val dir = getRevealDirection(from, to)
        Timber.d("$from - $to -> $dir")
        revealThresholds(dir!!)
    }

    val minFactor =
        if (RevealDirection.EndToStart in directions) SwipeableDefaults.StandardResistanceFactor else SwipeableDefaults.StiffResistanceFactor
    val maxFactor =
        if (RevealDirection.StartToEnd in directions) SwipeableDefaults.StandardResistanceFactor else SwipeableDefaults.StiffResistanceFactor

    Box(
        Modifier.swipeable(
            state = state,
            anchors = anchors,
            thresholds = thresholds,
            orientation = Orientation.Horizontal,
            enabled = state.currentValue == Default,
            reverseDirection = isRtl,
            resistance = ResistanceConfig(
                basis = width,
                factorAtMin = minFactor,
                factorAtMax = maxFactor
            )
        )
    ) {

        Row {
            startButton?.let {

                val horPadding = 16.dp
                val scale = 1f
//                val scale = if (state.isAnimationRunning) 1f else when (abs(state.offset.value)) {
//                    in 0f..with(LocalDensity.current) { horPadding.toPx() } -> 1f
//                    in revealWidth..width -> 1f
//                    else -> 1f + abs(state.offset.value) / (revealWidth * 2f)
//                }

//                    val alignment =
//                        if (it.direction == RevealDirection.EndToStart) Alignment.CenterEnd else Alignment.CenterStart
                Icon(
                    it.icon,
                    contentDescription = it.contentDescription,
                    Modifier
                        .background(color = it.backgroundColor)
                        .size(max(height, it.width ?: height).dp)
                        .padding(horizontal = horPadding, vertical = 0.dp)
                        .scale(scale)
                        .align(Alignment.CenterVertically),
                    tint = it.iconTint
                        ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                )


            }

            Spacer(modifier = Modifier.weight(1f))
            endButton?.let {

                val horPadding = 16.dp
                val scale = 1f
//                val scale = if (state.isAnimationRunning) 1f else when (abs(state.offset.value)) {
//                    in 0f..with(LocalDensity.current) { horPadding.toPx() } -> 1f
//                    in revealWidth..width -> 1f
//                    else -> 1f + abs(state.offset.value) / (revealWidth * 2f)
//                }

//                    val alignment =
//                        if (it.direction == RevealDirection.EndToStart) Alignment.CenterEnd else Alignment.CenterStart
                Icon(
                    it.icon,
                    contentDescription = it.contentDescription,
                    Modifier
                        .background(color = it.backgroundColor)
//                            .align(alignment)
                        .padding(horizontal = horPadding, vertical = 0.dp)
                        .scale(scale),
                    tint = it.iconTint
                        ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                )
            }
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(state.offset.value.roundToInt(), 0) }
                .onSizeChanged { size ->
                    height = size.height.toFloat()
                },
            content = revealContent
        )
    }

}

class RevealButton(
    val icon: ImageVector,
    val iconTint: Color? = null,
    val backgroundColor: Color = Color.Transparent,
    val width: Float? = null,
    val contentDescription: String? = null
)

private fun getRevealDirection(from: RevealValue, to: RevealValue): RevealDirection? {
    return when {
        // settled at the default state
        from == to && from == Default -> null
        // has been dismissed to the end
        from == to && from == RevealedToEnd -> RevealDirection.StartToEnd
        // has been dismissed to the start
        from == to && from == RevealedToStart -> RevealDirection.EndToStart
        // is currently being dismissed to the end
        from == Default && to == RevealedToEnd -> RevealDirection.StartToEnd
        // is currently being dismissed to the start
        from == Default && to == RevealedToStart -> RevealDirection.EndToStart
        // has been dismissed to the end but is now animated back to default
        from == RevealedToEnd && to == Default -> RevealDirection.StartToEnd
        // has been dismissed to the start but is now animated back to default
        from == RevealedToStart && to == Default -> RevealDirection.EndToStart
        to == CommitedToEnd -> RevealDirection.StartToEnd
        to == CommitedToStart -> RevealDirection.EndToStart
        else -> null
    }
}
