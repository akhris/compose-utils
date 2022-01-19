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
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.swipetoreveal.*
import com.akhris.composeutils.ui.theme.ComposeUtilsTheme
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
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
    private fun TestList() {

        val startIndx = remember { 12 }
        val endIndx = remember { 20 }
        var peekStart by remember { mutableStateOf(-1) }
        var peekEnd by remember { mutableStateOf(-1) }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
        ) {

            Button(onClick = {
                peekStart = Random.nextInt(range = startIndx..endIndx)
            }) {
                Text(text = "peek start to end!")
            }

            Button(onClick = {
                peekEnd = Random.nextInt(range = startIndx..endIndx)
            }) {
                Text(text = "peek end to start!")
            }

            for (i in startIndx..endIndx) {
                MakeRevealItem(
                    index = i,
                    withSecondaryText = i == (startIndx + 1),
                    peekStart = i == peekStart,
                    peekEnd = i == peekEnd
                )
            }

        }

    }
}

@ExperimentalMaterialApi
@Composable
fun MakeRevealItem(
    index: Int,
    withSecondaryText: Boolean = false,
    peekStart: Boolean = false,
    peekEnd: Boolean = false
) {
    val revealState = rememberRevealState()
    var startIcon1 by remember { mutableStateOf(true) }
    var startIcon2 by remember { mutableStateOf(true) }
    var endIcon1 by remember { mutableStateOf(true) }
    var endIcon2 by remember { mutableStateOf(true) }

    val startButton1 = remember(startIcon1) {
        IconRevealButton(
            icon = when (startIcon1) {
                true -> Icons.Rounded.Favorite
                false -> Icons.Rounded.FavoriteBorder
            },
            backgroundColor = Color.Yellow,
            callback = {
                startIcon1 = !startIcon1
            }
        )
    }

    val startButton2 = remember(startIcon2) {
        IconRevealButton(
            icon = when (startIcon2) {
                true -> Icons.Rounded.Check
                false -> Icons.Rounded.CheckCircle
            },
            backgroundColor = Color.LightGray,
            callback = {
                startIcon2 = !startIcon2
            }
        )
    }


    val endButton1 = remember(endIcon1) {
        IconRevealButton(
            icon = when (endIcon1) {
                true -> Icons.Rounded.AccountBox
                false -> Icons.Rounded.AccountCircle
            },
            backgroundColor = Color.Magenta,
            callback = {
                endIcon1 = !endIcon1
            }
        )
    }
    val endButton2 = remember(endIcon2) {
        IconRevealButton(
            icon = when (endIcon2) {
                true -> Icons.Rounded.Delete
                false -> Icons.Rounded.Delete
            },
            backgroundColor = Color.Red,
            callback = {
                endIcon2 = !endIcon2
            }
        )
    }

    SwipeToReveal(
        state = revealState,
        modifier = Modifier.fillMaxWidth(),
        startButtons = listOf(startButton1, startButton2, endButton1),
        endButtons = listOf(endButton2,endButton1, startButton1),
        startButtonsBehavior = if (withSecondaryText) OverlappingButtonsBehavior() else StretchingButtonsBehavior(),
        endButtonsBehavior = if (withSecondaryText) OverlappingButtonsBehavior() else StretchingButtonsBehavior()
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
                        text = "$index. List item with reveal"
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