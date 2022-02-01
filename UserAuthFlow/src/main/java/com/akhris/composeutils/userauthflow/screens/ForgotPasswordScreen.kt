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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.akhris.composeutils.userauthflow.R
import com.akhris.composeutils.userauthflow.auth.AuthState
import com.akhris.composeutils.userauthflow.auth.EmailField
import com.akhris.composeutils.userauthflow.composables.ProgressButton
import com.akhris.composeutils.userauthflow.utils.isValidEmail

@Composable
fun ForgotPasswordScreen(
    eMail: String = "",
    onEmailChanged: ((String) -> Unit)? = null,
    onRestorePasswordClicked: ((eMail: String) -> Unit)? = null,
    state: AuthState.ForgotPassword? = null
) {

    var errorChecksEnabled by remember { mutableStateOf(false) }

    var isEmailValid by remember(eMail) {
        mutableStateOf(eMail.isValidEmail())
    }

    LaunchedEffect(key1 = state) {
        when (state) {
            AuthState.ForgotPassword.Failure.EMailNotExists -> {
                isEmailValid = false
            }
            else -> {
                //do nothing
            }
        }
    }

    Column {
        //email field
        EmailField(
            userEmail = eMail,
            onEmailChanged = { onEmailChanged?.invoke(it) },
            isEmailValid = if (errorChecksEnabled) isEmailValid else true,
            errorRes = when (state) {
                AuthState.ForgotPassword.Failure.EMailNotExists -> R.string.user_auth_email_not_found
                else -> R.string.user_auth_wrong_email
            }
        )

        //send code button
        Box(modifier = Modifier.align(Alignment.End)) {
            ProgressButton(
                isProgress = state == AuthState.ForgotPassword.SendingCodeInProgress,
                isEnabled = !errorChecksEnabled || isEmailValid,
                onClick = {
                    if (!errorChecksEnabled) {
                        errorChecksEnabled = true
                    }
                    if (isEmailValid)
                        onRestorePasswordClicked?.invoke(eMail)
                }, buttonTextRes = R.string.user_auth_forgot_password_send_code
            )
        }
    }

}