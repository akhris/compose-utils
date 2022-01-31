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

package com.akhris.composeutils.userauthflow.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.EmailField
import com.akhris.composeutils.userauthflow.utils.isValidEmail

@Composable
internal fun InitialScreen(
    eMail: String = "",
    onEmailChanged: ((String) -> Unit)? = null,
    onForgotPasswordClicked: (() -> Unit)? = null,
    onSignUpClicked: (() -> Unit)? = null,
    onSignInClicked: (() -> Unit)? = null
) {

    Column {
        //email field
        EmailField(
            userEmail = eMail,
            onEmailChanged = { onEmailChanged?.invoke(it) },
            isValidEmail = eMail.isValidEmail()
        )


        Row {
            onForgotPasswordClicked?.let {
                TextButton(onClick = it) {
                    Text(text = stringResource(id = R.string.user_auth_forgot_password))
                }
            }
            onSignUpClicked?.let {
                TextButton(onClick = it) {
                    Text(text = stringResource(id = R.string.user_auth_sign_up))
                }
            }
            onSignInClicked?.let {
                Button(onClick = it) {
                    Text(text = stringResource(id = R.string.user_auth_sign_in))
                }
            }
        }
    }

}