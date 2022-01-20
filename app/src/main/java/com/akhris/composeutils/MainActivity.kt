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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.swipetoreveal.*
import com.akhris.composeutils.ui.theme.ComposeUtilsTheme

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

        var peekStart by remember { mutableStateOf(false) }
        var peekEnd by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
        ) {

            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                peekStart = !peekStart
            }) {
                Text(text = "peek start to end!")
            }

            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                peekEnd = !peekEnd
            }) {
                Text(text = "peek end to start!")
            }


            MakeRevealItem(
                text = "Stretching buttons",
                peekStart = peekStart,
                peekEnd = peekEnd,
                buttonBehavior = StretchingBehavior(),
                onStartPeeked = { peekStart = false },
                onEndPeeked = { peekEnd = false }
            )
            MakeRevealItem(
                text = "Overlapping buttons",
                withSecondaryText = true,
                buttonBehavior = OverlappingBehavior()
            )
            MakeRevealItem(
                text = "Fixed buttons",
                buttonBehavior = FixedPositionBehavior()
            )

            MakeRevealItem(
                text = "Overlapping with no auto-firing",
                buttonBehavior = OverlappingBehavior(),
                withCommit = false
            )

        }

    }

    @ExperimentalMaterialApi
    @Composable
    fun MakeRevealItem(
        text: String = "List item with reveal",
        withSecondaryText: Boolean = false,
        peekStart: Boolean = false,
        peekEnd: Boolean = false,
        buttonBehavior: IButtonBehavior,
        onStartPeeked: (() -> Unit)? = null,
        onEndPeeked: (() -> Unit)? = null,
        withCommit: Boolean = true
    ) {
        val revealState = rememberRevealState()

        var isFavorite by remember { mutableStateOf(false) }

        val startButtons = listOf(
            IconRevealButton(
                icon = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                callback = {
                    isFavorite = !isFavorite
                },
                backgroundColor = Color.Yellow,
                startCornersRadius = 4.dp
            ),
            IconRevealButton(
                icon = Icons.Rounded.Edit,
                callback = {

                },
                backgroundColor = Color.Magenta
            )
        )

        val endButtons = listOf(
            IconRevealButton(
                icon = Icons.Rounded.Delete,
                callback = {},
                iconTint = Color.Red,
                endCornersRadius = 4.dp
            ),
            IconRevealButton(
                icon = Icons.Rounded.Share,
                callback = {}
            ),
            IconRevealButton(
                icon = Icons.Rounded.Settings,
                callback = {}
            )
        )


        SwipeToReveal(
            state = revealState,
            modifier = Modifier.fillMaxWidth(),
            startButtons = startButtons,
            endButtons = endButtons,
            startButtonsBehavior = buttonBehavior,
            endButtonsBehavior = buttonBehavior,
            withEndCommit = withCommit,
            withStartCommit = withCommit
        ) {
            Card {
                ListItem(
                    secondaryText = if (withSecondaryText) {
                        {
                            Text(text = "WIDE ELEMENT")
                        }
                    } else null,
                    text = {
                        Text(
                            text = text
                        )
                    })
            }
        }

        LaunchedEffect(key1 = peekStart, key2 = peekEnd) {
            if (peekStart) {
                revealState.peek(RevealDirection.StartToEnd)
                onStartPeeked?.invoke()
            }
            if (peekEnd) {
                revealState.peek(RevealDirection.EndToStart)
                onEndPeeked?.invoke()
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