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

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.akhris.composeutils.userauthflow.universal.buttons.Button
import com.akhris.composeutils.userauthflow.universal.buttons.ButtonType

@Composable
fun RenderButton(button: Button) {
    when (button.type) {
        ButtonType.Primary -> RenderPrimaryButton(button = button)
        ButtonType.Text -> RenderTextButton(button = button)
    }
}

@Composable
private fun RenderPrimaryButton(button: Button) {
    Button(onClick = { }) {
        Text(text = stringResource(id = button.textRes))
    }
}

@Composable
private fun RenderTextButton(button: Button) {
    TextButton(onClick = { }) {
        Text(text = stringResource(id = button.textRes))
    }
}
