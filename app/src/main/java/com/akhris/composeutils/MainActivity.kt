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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ThumbUp
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
    var startIcon by remember { mutableStateOf(true) }
    var endIcon by remember { mutableStateOf(true) }

    val startButton = remember(startIcon) {
        IconRevealButton(
            icon = when (startIcon) {
                true -> Icons.Rounded.Favorite
                false -> Icons.Rounded.FavoriteBorder
            },
            backgroundColor = Color.Yellow,
            callback = {
                startIcon = !startIcon
            }
        )
    }

    val endButton = remember(endIcon) {
        IconRevealButton(
            icon = when (endIcon) {
                true -> Icons.Rounded.ThumbUp
                false -> Icons.Rounded.ArrowDropDown
            },
            backgroundColor = Color.Magenta,
            callback = {
                endIcon = !endIcon
            }
        )
    }
    SwipeToReveal(
        state = revealState,
        modifier = Modifier.fillMaxWidth(),
        startButtons = listOf(startButton, endButton),
        endButtons = listOf(endButton)
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