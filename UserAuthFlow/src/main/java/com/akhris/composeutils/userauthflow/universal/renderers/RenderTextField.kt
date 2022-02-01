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

package com.akhris.composeutils.userauthflow.universal.renderers

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.universal.fields.EndIconType
import com.akhris.composeutils.userauthflow.universal.fields.TextField

@Composable
fun RenderTextField(
    textField: TextField,
    text: String,
    onTextChanged: ((String) -> Unit)? = null,
    isValid: Boolean = true,
    @StringRes errorRes: Int? = null
) {

    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
//                    .testTag(textTestTag)
            ,
            value = text,
            isError = !isValid,
            onValueChange = {
                onTextChanged?.invoke(it)
            },
            label = textField.hintRes?.let { { Text(stringResource(id = it)) } },
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            visualTransformation = when (textField.endIconType) {
                EndIconType.VisibilityToggle -> {
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                }
                else -> VisualTransformation.None
            },
            trailingIcon =
            when (textField.endIconType) {
                EndIconType.ClearText -> {
                    if (text.isNotEmpty()) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    onTextChanged?.invoke("")
                                })
                        }
                    } else null
                }
                EndIconType.VisibilityToggle -> {
                    {
                        Icon(
                            painter = if (passwordVisible) {
                                painterResource(id = R.drawable.ic_round_visibility_24)
                            } else {
                                painterResource(id = R.drawable.ic_round_visibility_off_24)
                            },
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                passwordVisible = !passwordVisible
                            })
                    }
                }
                else -> null
            },
            keyboardOptions = textField.keyboardOptions
        )

        if (!isValid && errorRes != null) {
            Text(
                text = stringResource(id = errorRes),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 16.dp)
//                    .testTag(errorTestTag)
            )
        }
    }
}