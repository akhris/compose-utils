package com.akhris.composeutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        val revealState = rememberRevealState(confirmStateChange = {
            false
        })
        val buttons = remember {
            setOf(
                RevealButton(
                    direction = RevealDirection.StartToEnd,
                    icon = Icons.Rounded.Favorite,
                    doOnReveal = {

                    }
                )
            )
        }
        Column(Modifier.fillMaxWidth()) {
            SwipeToReveal(
                state = revealState,
                modifier = Modifier.fillMaxWidth(),
                buttons = buttons
            ) {
                Card {
                    ListItem(
                        overlineText = {
                                       Text(text = "${revealState.offset.value}")
                        },
                        secondaryText = {
                            Text(text = " ${revealState.isRevealed(
                                    RevealDirection.StartToEnd)}")
                        },
                        text = {
                        Text(
                            text = "${revealState.currentValue}"
                        )
                    })
                }
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