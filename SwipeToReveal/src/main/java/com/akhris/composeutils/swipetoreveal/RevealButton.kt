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

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

abstract class RevealButton(
    val callback: () -> Unit
) {
    @Composable
    abstract fun Background(boxScope: BoxScope)

    @Composable
    abstract fun Foreground(boxScope: BoxScope)
}

class IconRevealButton(
    val icon: ImageVector,
    val iconTint: Color? = null,
    val backgroundColor: Color? = null,
    val contentDescription: String? = null,
    callback: () -> Unit
) : RevealButton(callback = callback) {

    @Composable
    override fun Foreground(boxScope: BoxScope) {
        boxScope.apply {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint
                    ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            )
        }
    }

    @Composable
    override fun Background(boxScope: BoxScope) {
        boxScope.apply {
            backgroundColor?.let {bgColor->
                Surface(
                    modifier = Modifier.matchParentSize(),
                    color = bgColor,
                    shape = RoundedCornerShape(4.dp)
                ) {

                }
            }
        }
    }


}