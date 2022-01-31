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

package com.akhris.composeutils.userauthflow.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ProgressButton(
    isProgress: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    buttonTextRes: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp),
                color = MaterialTheme.colors.onSurface
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            modifier = Modifier
                .padding(vertical = 8.dp),
            enabled = isEnabled && !isProgress,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text(text = stringResource(id = buttonTextRes))
        }
    }
}