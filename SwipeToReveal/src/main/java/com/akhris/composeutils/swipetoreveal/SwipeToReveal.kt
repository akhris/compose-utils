/*
 * Copyright (c) 2022. Anatoly Khristianovsky.  All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akhris.composeutils.swipetoreveal

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.swipetoreveal.RevealValue.*
import kotlinx.coroutines.CancellationException
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
@Suppress("unused")
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
 * @param startButtons - buttons at the start (left) position
 * @param endButtons - buttons at the end (right) position
 * @param startButtonsBehavior - start button's width and offset behaviors, see [IButtonBehavior] for details
 * @param endButtonsBehavior - end button's width and offset behaviors, see [IButtonBehavior] for details
 * @param withStartCommit - if true, drag further to the end to fire first start button's action
 * @param withEndCommit - if true, drag further to the start to fire first end button's action
 */
@Composable
@ExperimentalMaterialApi
fun SwipeToReveal(
    modifier: Modifier = Modifier,
    state: RevealState = rememberRevealState(),
    startButtons: List<RevealButton> = listOf(),
    endButtons: List<RevealButton> = listOf(),
    startButtonsBehavior: IButtonBehavior = StretchingBehavior(),
    endButtonsBehavior: IButtonBehavior = StretchingBehavior(),
    withStartCommit: Boolean = true,
    withEndCommit: Boolean = true,
    overdrag: Dp? = null,
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
        startButtons,
    ) {
        iconSlotSize * startButtons.size
    }
    //max revealing width
    val revealWidthStartPx = with(LocalDensity.current) { revealWidthStartDp.toPx() }

    val commitWidthStartDp =
        remember(revealWidthStartDp, overdrag, iconSlotSize, withStartCommit) {
            if (withStartCommit) {
                revealWidthStartDp + (overdrag ?: iconSlotSize)
            } else null
        }

    val commitWidthStartPx =
        with(LocalDensity.current) { commitWidthStartDp?.toPx() }

    val revealWidthEndDp = remember(
        iconSlotSize,
        endButtons
    ) {
        iconSlotSize * endButtons.size
    }
    //max revealing width
    val revealWidthEndPx = with(LocalDensity.current) { revealWidthEndDp.toPx() }

    val commitWidthEndDp = remember(revealWidthEndDp, overdrag, iconSlotSize) {

        if (withEndCommit) {
            revealWidthEndDp + (overdrag ?: iconSlotSize)
        } else null
    }

    val commitWidthEndPx =
        with(LocalDensity.current) { commitWidthEndDp?.toPx() }


    val revealThresholds: (RevealDirection) -> ThresholdConfig = { FractionalThreshold(0.5f) }


    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val anchors = remember(revealWidthStartPx, commitWidthStartPx) {
        val map = mutableMapOf(0f to Default)
        if (RevealDirection.StartToEnd in directions) {
            map += revealWidthStartPx to RevealedToEnd
            commitWidthStartPx?.let {
                map += commitWidthStartPx to CommitedToEnd
            }
        }
        if (RevealDirection.EndToStart in directions) {
            map += -revealWidthEndPx to RevealedToStart
            commitWidthEndPx?.let {
                map += -commitWidthEndPx to CommitedToStart
            }
        }
        map
    }

    val thresholds = { from: RevealValue, to: RevealValue ->
        val dir = getRevealDirection(from, to)
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

        //background box (left and right):
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            when (state.revealDirection) {
                RevealDirection.StartToEnd -> {
                    //draw start buttons:
                    if (startButtons.isNotEmpty()) {
                        DrawButtonGroup(
                            buttons = startButtons,
                            isFromStart = true,
                            state = state,
                            commitWidthPx = commitWidthStartPx,
                            buttonsBehavior = startButtonsBehavior,
                            onReset = { isReset = true },
                            width = width
                        )
                    }
                }

                RevealDirection.EndToStart -> {
                    //draw end buttons:
                    if (endButtons.isNotEmpty()) {
                        DrawButtonGroup(
                            buttons = endButtons,
                            isFromStart = false,
                            state = state,
                            commitWidthPx = commitWidthEndPx,
                            buttonsBehavior = endButtonsBehavior,
                            onReset = { isReset = true },
                            width = width
                        )
                    }
                }
                null -> {
                    //do nothing
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
                state.reset()
                startButtons.firstOrNull()?.callback?.invoke()
            }
            CommitedToStart -> {
                state.reset()
                endButtons.firstOrNull()?.callback?.invoke()
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
 * Drawing buttons group that are on the start-side or from the end-side.
 */
@ExperimentalMaterialApi
@Composable
private fun DrawButtonGroup(
    buttons: List<RevealButton>,
    isFromStart: Boolean,
    state: RevealState,
    commitWidthPx: Float?,
    buttonsBehavior: IButtonBehavior,
    onReset: () -> Unit,
    width: Float
) {
    var previousButtonWidth = 0f
    buttons.forEachIndexed { index, button ->


        val animation = remember<AnimationSpec<Float>>(commitWidthPx, state.offset.value) {
            if (commitWidthPx == null || abs(state.offset.value) < commitWidthPx) {
                //no animation is required
                tween(durationMillis = 0, easing = LinearEasing)
            } else {
                spring()
            }
        }


        val currentButtonWidth by animateFloatAsState(
            targetValue =
            if (commitWidthPx != null && abs(state.offset.value) >= commitWidthPx && index == 0) {
                abs(state.offset.value)
            } else
                buttonsBehavior.getWidth(
                    index,
                    state.offset.value,
                    buttons.size
                ),
            animationSpec = animation
        )


        var currentButtonOffset =
            if (commitWidthPx != null && abs(state.offset.value) >= commitWidthPx) {
                previousButtonWidth
            } else
                buttonsBehavior.getXOffset(
                    index,
                    state.offset.value,
                    buttons.size
                )


        if (!isFromStart) {
            currentButtonOffset = width - currentButtonWidth - currentButtonOffset
        }

        DrawButtonBox(
            button,
            width = currentButtonWidth,
            xOffset = currentButtonOffset,
        ) {
            onReset()
            button.callback.invoke()
        }
        previousButtonWidth += currentButtonWidth

    }
}

/**
 * Draws [Box] with two layers:
 * - background layer from [RevealButton] object - see [RevealButton.Background] for details
 * - front layer icon from [RevealButton] object - see [RevealButton.Foreground] for details
 */
@Composable
private fun DrawButtonBox(
    button: RevealButton,
    width: Float,
    xOffset: Float,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .fillMaxHeight()
            .width(
                with(LocalDensity.current) { width.toDp() }
            )
            .offset(x = with(LocalDensity.current) { xOffset.toDp() })
            .clickable {
                onClick()
            }
    ) {
        button.Background(boxScope = this)
        button.Foreground(boxScope = this)
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
