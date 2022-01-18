package com.akhris.composeutils.swipetoreveal

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.swipetoreveal.RevealValue.*
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import kotlin.math.abs
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

    private val peekAnimationSpec =
        SpringSpec<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )

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

    /**
     * Reveal the component in the given [direction] with an animation given by [animationSpec]
     * without triggering the button's action and swipe back programmatically.
     * Use it in help purposes to show user that this item can be swiped.
     */
    suspend fun peek(
        direction: RevealDirection,
        animationSpec: AnimationSpec<Float> = peekAnimationSpec
    ) {
        val targetValue =
            if (direction == RevealDirection.StartToEnd) RevealedToEnd else RevealedToStart
        animateTo(targetValue = targetValue, anim = animationSpec)
        animateTo(targetValue = Default, anim = animationSpec)
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
 * A composable that can be revealed by swiping left or right.
 *
 * @param state The state of this component.
 * @param modifier Optional [Modifier] for this component.
 * @param revealContent The content that can be dismissed.
 * @param startButton - button at the start (left) position
 * @param endButton - button at the end (right) position
 */
@Composable
@ExperimentalMaterialApi
fun SwipeToReveal(
    modifier: Modifier = Modifier,
    state: RevealState = rememberRevealState(),
    startButtons: List<RevealButton> = listOf(),
    endButtons: List<RevealButton> = listOf(),
    overdrag: Int = 40,
    revealContent: @Composable BoxScope.() -> Unit
) = BoxWithConstraints(modifier) {


    val directions = remember(startButtons, endButtons) {
        val dirs = mutableListOf<RevealDirection>()
        if (startButtons.isNotEmpty()) {
            dirs.add(RevealDirection.StartToEnd)
        }
        if (endButtons.isNotEmpty()) {
            dirs.add(RevealDirection.EndToStart)
        }
        dirs.toList()
    }

    val iconHorPadding = remember { 32.dp }
    val iconSize = remember { 24.dp }

    //width of the composable
    val width = remember { constraints.maxWidth.toFloat() }

    val iconSlotSize = remember(iconSize, iconHorPadding) { iconSize + iconHorPadding * 2 }

    val revealWidthStartDp = remember(
        iconSlotSize,
        startButtons
    ) { iconSlotSize * startButtons.size }
    //max revealing width
    val revealWidthStartPx = with(LocalDensity.current) { revealWidthStartDp.toPx() }
    val commitWidthStart = remember(revealWidthStartPx, overdrag) { revealWidthStartPx + overdrag }

    val revealWidthEndDp = remember(
        iconSlotSize,
        endButtons
    ) { iconSlotSize * endButtons.size }
    //max revealing width
    val revealWidthEndPx = with(LocalDensity.current) { revealWidthEndDp.toPx() }
    val commitWidthEnd = remember(revealWidthStartPx, overdrag) { revealWidthEndPx + overdrag }


    val revealThresholds: (RevealDirection) -> ThresholdConfig = { FractionalThreshold(0.5f) }


    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val anchors = remember(revealWidthStartPx, commitWidthStart) {
        val map = mutableMapOf(0f to Default)
        if (RevealDirection.StartToEnd in directions) {
            map += revealWidthStartPx to RevealedToEnd
            map += commitWidthStart to CommitedToEnd
        }
        if (RevealDirection.EndToStart in directions) {
            map += -revealWidthEndPx to RevealedToStart
            map += -commitWidthEnd to CommitedToStart
        }
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

    //reset flag - used when there is need for reset state to Default
    var isReset by remember { mutableStateOf(false) }

    Box(
        Modifier
            .height(IntrinsicSize.Min)  //needed for children's fillMaxHeight
            .swipeable(
                state = state,
                anchors = anchors,
                thresholds = thresholds,
                orientation = Orientation.Horizontal,
                enabled = true,
                reverseDirection = isRtl,
                resistance = ResistanceConfig(
                    basis = width,
                    factorAtMin = minFactor,
                    factorAtMax = maxFactor
                )
            )
    ) {

        //background rows (left and right):
        when (state.revealDirection) {
            RevealDirection.StartToEnd -> {
                val scale = if (state.isAnimationRunning) 1f else when (val curOffset =
                    abs(state.offset.value)) {
                    in 0f..revealWidthStartPx -> 1f
                    in revealWidthStartPx..commitWidthStart -> curOffset / revealWidthStartPx
                    else -> 1f
                }
                //draw start buttons:
                if (startButtons.isNotEmpty()) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .width(revealWidthStartDp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        startButtons.forEachIndexed { index, button ->
                            DrawButtonBox(
                                button,
                                width = iconSlotSize,
                                iconHorPadding = iconHorPadding,
                                scale = if (index == 0) scale else 1f
                            ) {
                                button.callback.invoke()
                                isReset = true
                            }
                        }

                    }


                }
            }
            RevealDirection.EndToStart -> {
                //draw end buttons:
                val scale = if (state.isAnimationRunning) 1f else when (val curOffset =
                    abs(state.offset.value)) {
                    in 0f..revealWidthEndPx -> 1f
                    in revealWidthEndPx..commitWidthEnd -> curOffset / revealWidthEndPx
                    else -> 1f
                }
                if (endButtons.isNotEmpty()) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .width(revealWidthEndDp)
                            .align(Alignment.CenterEnd),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        endButtons.forEachIndexed { index, button ->
                            DrawButtonBox(
                                button,
                                width = iconSlotSize,
                                iconHorPadding = iconHorPadding,
                                scale = if (index == 0) scale else 1f
                            ) {
                                button.callback.invoke()
                                isReset = true
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .wrapContentHeight()
                .offset { IntOffset(state.offset.value.roundToInt(), 0) }
                .clickable {
                    isReset = true
                },
            content = revealContent
        )
    }



    LaunchedEffect(key1 = state.currentValue) {
        when (state.currentValue) {
            CommitedToEnd -> {
                startButtons.firstOrNull()?.callback?.invoke()
                state.reset()
            }
            CommitedToStart -> {
                endButtons.firstOrNull()?.callback?.invoke()
                state.reset()
            }
            else -> {
                //do nothing
            }
        }
    }

    LaunchedEffect(key1 = isReset) {
        if (isReset) {
            state.reset()
            isReset = false
        }
    }

}


/**
 * Draws [Box] with two layers:
 * - background layer from [RevealButton] object - see [RevealButton.background] for details
 * - front layer icon from [RevealButton] icon's parameters depending
 */
@Composable
private fun RowScope.DrawButtonBox(
    button: RevealButton,
    width: Dp,
    iconHorPadding: Dp,
    scale: Float,
    onClick: () -> Unit,
) {
    Box(
        Modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .fillMaxHeight()
            .width(width)
            .clickable {
                onClick()
            }
    ) {
        button.Background(boxScope = this, scale)
        button.Foreground(boxScope = this, scale)
    }
}

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
