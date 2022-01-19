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

import kotlin.math.abs

/**
 * Interface of button's behavior.
 * Describes button's width and position depending on front layer position and total buttons count.
 */
interface IButtonBehavior {
    fun getWidth(buttonIndex: Int, frontLayerOffset: Float, totalButtonsCount: Int): Float
    fun getXOffset(buttonIndex: Int, frontLayerOffset: Float, totalButtonsCount: Int): Float
}

class StretchingButtonsBehavior : IButtonBehavior {
    override fun getWidth(
        buttonIndex: Int,
        frontLayerOffset: Float,
        totalButtonsCount: Int
    ): Float {
        return abs(frontLayerOffset) / totalButtonsCount
    }

    override fun getXOffset(
        buttonIndex: Int,
        frontLayerOffset: Float,
        totalButtonsCount: Int
    ): Float {
        return buttonIndex * getWidth(buttonIndex, frontLayerOffset, totalButtonsCount)
    }
}

class OverlappingButtonsBehavior(private val fixedWidthPx: Float = 176f) : IButtonBehavior {
    override fun getWidth(
        buttonIndex: Int,
        frontLayerOffset: Float,
        totalButtonsCount: Int
    ): Float {
        return if (abs(frontLayerOffset) <= totalButtonsCount * fixedWidthPx){
            fixedWidthPx
        } else {
            abs(frontLayerOffset) / totalButtonsCount
        }

    }

    override fun getXOffset(
        buttonIndex: Int,
        frontLayerOffset: Float,
        totalButtonsCount: Int
    ): Float {
        return buttonIndex * abs(frontLayerOffset) / totalButtonsCount
    }

}