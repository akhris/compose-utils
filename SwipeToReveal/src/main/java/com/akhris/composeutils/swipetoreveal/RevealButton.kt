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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

abstract class RevealButton(
    val callback: () -> Unit
) {
    @Composable
    abstract fun Background(boxScope: BoxScope, scale: Float)

    @Composable
    abstract fun Foreground(boxScope: BoxScope, scale: Float)
}

class IconRevealButton(
    val icon: ImageVector,
    val iconTint: Color? = null,
    val backgroundColor: Color? = null,
    val contentDescription: String? = null,
    callback: () -> Unit
) : RevealButton(callback = callback) {

    @Composable
    override fun Foreground(boxScope: BoxScope, scale: Float) {
        boxScope.apply {
            Icon(
                modifier = Modifier
                    .scale(scale)
                    .align(Alignment.Center),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint
                    ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
            )
        }
    }

    @Composable
    override fun Background(boxScope: BoxScope, scale: Float) {
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