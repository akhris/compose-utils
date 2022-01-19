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

package com.akhris.composeutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.akhris.composeutils.swipetoreveal.*
import com.akhris.composeutils.ui.theme.ComposeUtilsTheme
import kotlin.random.Random
import kotlin.random.nextInt

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val pairIcons = listOf(
        Icons.Default.Favorite to Icons.Default.FavoriteBorder,
        Icons.Default.Done to Icons.Default.CheckCircle
    )
    private val icons = listOf(
        Icons.Default.Delete,
        Icons.Default.Share,
        Icons.Default.Done,
        Icons.Default.Edit
    )

    private val colors = listOf(
        Color.Green,
        Color.Yellow,
        Color.Red,
        Color.Magenta,
        Color.Cyan,
        Color.LightGray
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeUtilsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TestList()
                }
            }
        }
    }


    @Composable
    private fun TestList(itemCount: Int = 4) {

        var peekStart by remember { mutableStateOf(-1) }
        var peekEnd by remember { mutableStateOf(-1) }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
        ) {

            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                peekStart = Random.nextInt(range = 0 until itemCount)
            }) {
                Text(text = "peek start to end!")
            }

            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                peekEnd = Random.nextInt(range = 0 until itemCount)
            }) {
                Text(text = "peek end to start!")
            }

            for (i in 0 until itemCount)
                MakeRevealItem(
                    peekStart = i == peekStart,
                    peekEnd = i == peekEnd,
                    buttonBehavior = when (i % 3) {
                        0 -> StretchingBehavior()
                        1 -> OverlappingBehavior()
                        else -> FixedPositionBehavior()
                    },
                    startButtonsCount = (i % 3 + 1),
                    endButtonsCount = (i % 3 + 1)
                )


        }

    }

    private fun getRandomColor(): Color {
        return colors[Random.nextInt(colors.size)]
    }

    @ExperimentalMaterialApi
    @Composable
    fun MakeRevealItem(
        withSecondaryText: Boolean = false,
        peekStart: Boolean = false,
        peekEnd: Boolean = false,
        startButtonsCount: Int = 1,
        endButtonsCount: Int = 1,
        buttonBehavior: IButtonBehavior
    ) {
        val revealState = rememberRevealState()

        val pairedButtonIndex = remember { Random.nextInt(until = 2) }
        var pairedClicked by remember { mutableStateOf(false) }

        val startButtons = List(startButtonsCount) { index ->
            when (index) {
                0 -> IconRevealButton(
                    if (pairedClicked) pairIcons[pairedButtonIndex].first else pairIcons[pairedButtonIndex].second,
                    callback = {
                        pairedClicked = !pairedClicked
                    },
                    backgroundColor = getRandomColor()
                )
                else -> IconRevealButton(
                    icon = icons[icons.size % index],
                    callback = {},
                    backgroundColor = getRandomColor()
                )
            }
        }

        val endButtons = List(endButtonsCount) { index ->
            IconRevealButton(
                icon = icons[Random.nextInt(icons.size)],
                callback = {},
                backgroundColor = getRandomColor()
            )
        }


        SwipeToReveal(
            state = revealState,
            modifier = Modifier.fillMaxWidth(),
            startButtons = startButtons,
            endButtons = endButtons,
            startButtonsBehavior = buttonBehavior,
            endButtonsBehavior = buttonBehavior
        ) {
            Card {
                ListItem(
                    overlineText = {
                        Text(text = "${revealState.offset.value}")
                    },
                    secondaryText = if (withSecondaryText) {
                        {
                            Text(text = "WIDE ELEMENT")
                        }
                    } else null,
                    text = {
                        Text(
                            text = "List item with reveal"
                        )
                    })
            }
        }

        LaunchedEffect(key1 = peekStart, key2 = peekEnd) {
            if (peekStart) {
                revealState.peek(RevealDirection.StartToEnd)
            }
            if (peekEnd) {
                revealState.peek(RevealDirection.EndToStart)

            }
        }


    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeUtilsTheme {
        Greeting("Android")
    }
}