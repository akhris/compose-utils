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

package com.akhris.composeutils.userauthflow.universal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.akhris.composeutils.userauthflow.auth.AuthScreenPattern
import com.akhris.composeutils.userauthflow.universal.events.Event
import com.akhris.composeutils.userauthflow.universal.factories.*
import com.akhris.composeutils.userauthflow.universal.ids.DefaultIDs
import com.akhris.composeutils.userauthflow.universal.renderers.RenderButton
import com.akhris.composeutils.userauthflow.universal.renderers.RenderTextField
import com.akhris.composeutils.userauthflow.universal.viewmodels.AuthFlowViewModel


@Composable
fun MainRender(viewModel: AuthFlowViewModel) {
    val eventResult by remember(viewModel) { viewModel.eventResults }.observeAsState()

    val eventFlowResult by remember(viewModel) { viewModel.eventFlow }.collectAsState(initial = null)

    RenderScreen(screenID = DefaultIDs.Screens.main, outcomeEvent = {

    })

}

@Composable
fun RenderScreen(
    screenID: String,
    incomeEvent: Event? = null,
    outcomeEvent: ((Event) -> Unit)? = null,
    screenFactory: ScreenFactory = DefaultScreenFactory(),
    textFieldFactory: TextFieldFactory = DefaultTextFieldFactory(),
    buttonFactory: ButtonFactory = DefaultButtonFactory()
) {
    val screen = remember(screenID) { screenFactory.getEntity(screenID) }
    val textFields =
        remember(screen) {
            screen?.textFieldsIDs?.mapNotNull { textFieldFactory.getEntity(it) } ?: listOf()
        }
    val buttons =
        remember(screen) {
            screen?.buttonsIDs?.mapNotNull { buttonFactory.getEntity(it) } ?: listOf()
        }

    val texts = remember { mutableStateMapOf<String, String>() }
    val textsValids = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(key1 = incomeEvent) {
        when (incomeEvent) {
            is Event.TextValidationEvent -> {
                textsValids[incomeEvent.textFieldID] = incomeEvent.isValid
            }
            else -> {
                //do nothing
            }
        }
    }

    AuthScreenPattern(
        titleRes = screen?.titleRes
    ) {


        Column {
            textFields.forEach { field ->
                //render fields
                RenderTextField(
                    textField = field,
                    text = texts[field.id] ?: "",
                    onTextChanged = {
                        texts[field.id] = it
                    },
                    isValid = textsValids[field.id] ?: true
                )
            }
            Row {
                buttons.forEach { b ->
                    RenderButton(b, onClick = { bId ->
                        outcomeEvent?.invoke(Event.ButtonClickedEvent(bId, screenID))
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun TestDefaultScreen() {
    RenderScreen(DefaultIDs.Screens.main)
}

