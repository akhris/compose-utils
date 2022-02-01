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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.KeyboardType
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.AuthScreenPattern
import com.akhris.composeutils.userauthflow.universal.buttons.Button
import com.akhris.composeutils.userauthflow.universal.buttons.ButtonType
import com.akhris.composeutils.userauthflow.universal.events.IEventHandler
import com.akhris.composeutils.userauthflow.universal.factories.ButtonFactory
import com.akhris.composeutils.userauthflow.universal.factories.ScreenFactory
import com.akhris.composeutils.userauthflow.universal.factories.TextFieldFactory
import com.akhris.composeutils.userauthflow.universal.fields.EndIconType
import com.akhris.composeutils.userauthflow.universal.fields.TextField
import com.akhris.composeutils.userauthflow.universal.renderers.RenderButton
import com.akhris.composeutils.userauthflow.universal.renderers.RenderTextField
import com.akhris.composeutils.userauthflow.universal.viewmodels.AuthFlowViewModel


class AuthFlow(
    private val screenFactory: ScreenFactory,
    private val textFieldFactory: TextFieldFactory,
    private val buttonFactory: ButtonFactory,
    private val eventHandler: IEventHandler
) {

    @Composable
    fun MainRender(viewModel: AuthFlowViewModel) {
        val eventResult by remember(viewModel) { viewModel.eventResults }.observeAsState()



        RenderScreen(id =)

    }

    @Composable
    fun RenderScreen(id: String) {
        val screen = remember(id) { screenFactory.getEntity(id) }
        val textFields =
            remember(screen) { screen.textFieldsIDs.map { textFieldFactory.getEntity(it) } }
        val buttons = remember(screen) { screen.buttonsIDs.map { buttonFactory.getEntity(it) } }

        AuthScreenPattern(
            titleRes = screen.titleRes
        ) {
            val texts = remember { mutableStateMapOf<String, String>() }

            Column {
                textFields.forEach { field ->
                    //render fields
                    RenderTextField(
                        textField = field,
                        text = texts[field.id] ?: "",
                        onTextChanged = {
                            texts[field.id] = it

                        }
                    )
                }
                Row {
                    buttons.forEach {
                        RenderButton(it)
                    }
                }
            }
        }
    }
}

val emailField = TextField(
    id = "text_field_email",
    hintRes = R.string.user_auth_email_hint,
    endIconType = EndIconType.ClearText,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
)

val signInButton = Button(id = "button_sign_in", textRes = R.string.user_auth_sign_in)
val signUpButton =
    Button(id = "button_sign_up", textRes = R.string.user_auth_sign_up, type = ButtonType.Text)
val forgotPasswordButton = Button(
    id = "button_sign_in",
    textRes = R.string.user_auth_forgot_password,
    type = ButtonType.Text
)


